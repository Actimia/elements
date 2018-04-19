package se.tdfpro.elements.client;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.client.abilities.Abilities;
import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.client.Handshake;
import se.tdfpro.elements.net.Client;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.client.abilities.Ability;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameClient extends BasicGameState {
    public static final int ID = 1;
    public static final Map<String, Image> textures = loadTextures(Paths.get("assets", "sprites"));

    public final Camera camera = new Camera();
    private final Map<Integer, PhysicsEntity> entities = new HashMap<>();

    private final List<Ability> abilities = new ArrayList<>();

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

        net.send(new Handshake(config.getOrDefault("username", "Player")));
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        g.setAntiAlias(true);
        g.pushTransform();

        camera.project(g);
        entities.values().forEach(ent -> ent.render(gc, this, g));

        g.popTransform();

        abilities.forEach(a -> a.render(gc, this, g));
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) {
        var fDelta = delta / 1000f;
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        net.getCommands().forEach(cmd -> cmd.execute(this));
        entities.values().forEach(ent -> ent.updateClient(gc, this, fDelta));
        abilities.forEach(a -> a.updateClient(gc, this, fDelta));
    }

    @Override
    public int getID() {
        return ID;
    }

    public void createAbilities(Player player) {
        abilities.add(Abilities.FIREBALL.create(player, new Vec2(739, 800), Keybind.mouse(Input.MOUSE_LEFT_BUTTON)));
        abilities.add(Abilities.BLINK.create(player, new Vec2(805, 800), Keybind.key(Input.KEY_LSHIFT)));
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

    private static void loadTextures(Map<String, Image> res, File directory, String prefix) {
        Arrays.stream(directory.listFiles()).forEach(file -> {
            var name = file.getName();
            if (file.isDirectory()) {
                loadTextures(res, file, name + "-");
            } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
                name = prefix + file.getName().substring(0, file.getName().length() - 4);
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
