package se.tdfpro.elements.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.client.engine.Camera;
import se.tdfpro.elements.client.engine.entity.Entity;
import se.tdfpro.elements.client.engine.entity.Player;
import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.client.Handshake;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameClient extends BasicGameState {

    public Camera camera = new Camera();
    private Map<Integer, Entity> entities = new HashMap<>();
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

        entities.values().forEach(ent -> ent.renderInterface(gc, this, g));
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        net.getCommands().forEach(cmd -> cmd.execute(this));
        entities.values().forEach(ent -> ent.update(gc, this, delta));
    }

    @Override
    public int getID() {
        return 0;
    }

    public void addEntity(Entity ent) {
        entities.put(ent.getID(), ent);
    }

    public Entity getEntity(int eid) {
        return entities.get(eid);
    }

    public Map<Integer, Entity> getEntities() {
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
