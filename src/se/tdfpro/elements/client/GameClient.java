package se.tdfpro.elements.client;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.client.ui.InterfaceComponent;
import se.tdfpro.elements.client.ui.Label;
import se.tdfpro.elements.client.ui.PlayerInterface;
import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.client.Handshake;
import se.tdfpro.elements.net.Client;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameClient extends BasicGameState {
    private static final List<String> defaultColors = List.of("#ffa69e", "#5f0ab7", "#7cffcb", "#d98324", "#ff4e00");
    public static final int ID = 1;
    public static final Map<String, Image> textures = loadTextures(Paths.get("assets", "sprites"));

    // better solution needed here if two GameClient's are ever run on the same jvm
    private static long time = 0;

    public final Camera camera = new Camera();
    private final Map<Integer, PhysicsEntity> entities = new HashMap<>();
    private InterfaceComponent uiRoot = new Label(new Vec2(800, 800), () -> "Connecting").setCenteredHorizontal(true);
    private final Client net;
    private final Map<String, String> config;
    private int pid;

    public GameClient(Client net, Map<String, String> config) {
        this.net = net;
        this.config = config;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) {
        // clear the command cache before handshake
        net.getCommands();

        var hs = new Handshake();
        hs.username = config.getOrDefault("username", "Player");
        var color = Optional.ofNullable(config.get("color"))
            .orElseGet(() -> defaultColors.get((int) (defaultColors.size() * Math.random())))
            .replace("#", "");
        // bit fiddling is to force max alpha so players cant make themselves invisible
        hs.color = new Color(Integer.parseInt(color.substring(1), 16) | 0xff000000);
        net.send(hs);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        g.setAntiAlias(true);
        g.setClip(0, 0, gc.getWidth(), gc.getHeight());
        g.pushTransform();

        camera.project(g);
        entities.values().forEach(ent -> ent.render(gc, this, g));

        g.popTransform();

        g.setAntiAlias(false);
        uiRoot.render(gc, this, g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int millis) {
        GameClient.time += millis;
        var delta = millis / 1000f;
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        net.getCommands().forEach(cmd -> cmd.execute(this));
        entities.values().forEach(ent -> ent.updateClient(gc, this, delta));
        uiRoot.updateClient(gc, this, delta);
    }

    @Override
    public int getID() {
        return ID;
    }

    public void initialiseInterface(Player player) {
        uiRoot = new PlayerInterface(player);
    }

    public void addEntity(PhysicsEntity ent) {
        entities.put(ent.getEid(), ent);
    }

    public void deleteEntity(int eid) {
        entities.remove(eid);
    }

    public PhysicsEntity getEntity(int eid) {
        return entities.get(eid);
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void send(ClientCommand command) {
        net.send(command);
    }

    public static long getTime() { return time; }

    private static void loadTextures(Map<String, Image> res, File directory, String prefix) {
        Arrays.stream(directory.listFiles()).forEach(file -> {
            var name = file.getName();
            if (file.isDirectory()) {
                loadTextures(res, file, prefix + name + "-");
            } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
                name = prefix + name.substring(0, file.getName().length() - 4);
                try {
                    var image = new Image(file.toString());
                    Log.info("Texture '" + name + "' loaded.");
                    res.put(name, image);
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static Map<String, Image> loadTextures(Path path) {
        var res = new HashMap<String, Image>();
        loadTextures(res, path.toFile(), "");
        return res;
    }
}
