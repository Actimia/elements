package se.tdfpro.elements.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.net.Client;
import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.client.Handshake;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;

import java.util.HashMap;
import java.util.Map;

public class GameClient extends BasicGameState {
    public static final int ID = 1;

    public Camera camera = new Camera();
    private Map<Integer, PhysicsEntity> entities = new HashMap<>();
    private Client net;
    private int pid;

    public GameClient(Client net) {
        this.net = net;

        // clear the command cache before handshake
        net.getCommands();

        net.send(new Handshake("Actimia"));
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
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

    public void addEntity(PhysicsEntity ent) {
        entities.put(ent.getID(), ent);
    }

    public void deleteEntity(int eid) {
        entities.remove(eid);
    }

    public PhysicsEntity getEntity(int eid) {
        return entities.get(eid);
    }

    public Map<Integer, PhysicsEntity> getEntities() {
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
