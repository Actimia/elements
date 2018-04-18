package se.tdfpro.elements.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.server.physics.entity.ClientEntity;
import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.client.Handshake;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameClient extends BasicGameState {
    public static final int ID = 1;

    public Camera camera = new Camera();
    private Map<Integer, ClientEntity> entities = new HashMap<>();
    private Network net;
    private int pid;

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        try {
            net = new Network("localhost", 7777);
            var hs = new Handshake();
            String[] usernames = {"Actimia", "Eaan", "Stalin", "Voldemort", "Arthas"};
            hs.username = usernames[new Random().nextInt(usernames.length)];
            net.send(hs);
        } catch (IOException e) {
            e.printStackTrace();
            gc.exit();
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.setAntiAlias(true);

        g.pushTransform();
        camera.project(g);

        entities.values().forEach(ent -> ent.render(gc, this, g));

        g.popTransform();
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        var fdelta = delta / 1000f;
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        net.getCommands().forEach(cmd -> cmd.execute(this));
        entities.values().forEach(ent -> ent.updateClient(gc, this, fdelta));
    }

    @Override
    public int getID() {
        return ID;
    }

    public void addEntity(ClientEntity ent) {
        entities.put(ent.getID(), ent);
    }

    public ClientEntity getEntity(int eid) {
        return entities.get(eid);
    }

    public Map<Integer, ClientEntity> getEntities() {
        return entities;
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
}
