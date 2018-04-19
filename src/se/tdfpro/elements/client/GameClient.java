package se.tdfpro.elements.client;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.net.Client;
import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.client.Handshake;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.abilities.Ability;
import se.tdfpro.elements.server.physics.abilities.Blink;
import se.tdfpro.elements.server.physics.abilities.Fireball;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Player;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GameClient extends BasicGameState {
    public static final int ID = 1;
    private static final Path ASSET_FOLDER = Paths.get("assets");
    public static final Map<String, Image> textures = loadTextures();

    public final Camera camera = new Camera();
    private final Map<Integer, PhysicsEntity> entities = new HashMap<>();

    private final List<Ability> abilities = new ArrayList<>();

    private final Client net;
    private int pid;

    public GameClient(Client net) {
        this.net = net;

        // clear the command cache before handshake
        net.getCommands();

        net.send(new Handshake("Actimia"));
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) {
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        g.setAntiAlias(true);

        g.pushTransform();
        camera.project(g);

        entities.values().forEach(ent -> ent.render(gc, this, g));
        abilities.forEach(a -> a.render(gc, this, g));

        g.popTransform();
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
        abilities.add(new Fireball(player, new Vec2(800, 800), input -> input.isMouseButtonDown(0)));
        abilities.add(new Blink(player, new Vec2(850, 800), input -> input.isKeyDown(Input.KEY_LSHIFT)));
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

//    public Map<Integer, PhysicsEntity> getEntities() {
//        return entities;
//    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void send(ClientCommand command) {
        net.send(command);
    }


    private static Map<String, Image> loadTextures() {
        var res = new HashMap<String, Image>();
        Arrays.stream(ASSET_FOLDER.toFile().listFiles()).forEach(file -> {
            if (file.getName().endsWith(".png")){
                var name = file.getName().substring(0, file.getName().length() - 4);

                try {
                    var image = new Image(file.toString());
                    Log.info("Texture '" + name + "' loaded.");
                    res.put(name, image);
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        });

        return res;
    }

}
