package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.UpdateEntity;
import se.tdfpro.elements.server.physics.CollisionManifold;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.shapes.Ray;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;
import static se.tdfpro.elements.server.physics.CollisionManifold.checkCollision;

public class GameServer {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000 / TICKS;

    private long lastTickStart = 0;

    private Network networking;
    private Map<Integer, PhysicsEntity> entities = new HashMap<>();

    public GameServer(int port) throws IOException {
        this.networking = new Network(port);

        var area = new Vec2(1600, 1000);

        var top = new Ray(new Vec2(0, 0), Vec2.RIGHT);
        var bottom = new Ray(new Vec2(0, area.y), Vec2.RIGHT);
        var left = new Ray(new Vec2(0, 0), Vec2.DOWN);
        var right = new Ray(new Vec2(area.x, 0), Vec2.DOWN);

        addEntity(top);
        addEntity(bottom);
        addEntity(left);
        addEntity(right);
    }

    public void run() {
        while (true) {

            var delta = (System.currentTimeMillis() - lastTickStart) / 1000f;
            lastTickStart = System.currentTimeMillis();

            executeCommands();
            updatePhysics(delta);
            var frametime = System.currentTimeMillis() - lastTickStart;
            if (frametime > TICK_TIME) {
                System.out.println("Very long frame warning: " + frametime + "ms");
            } else if(frametime > TICK_TIME / 2) {
                System.out.println("Long frame warning: " + frametime + "ms");
            }
            var idletime = TICK_TIME - frametime;
            if (idletime > 0) {
                try {
                    sleep(idletime);
                } catch (InterruptedException ignored) {
                }
            }

        }
    }

    public void spawnEntity(PhysicsEntity ent) {
        entities.put(ent.eid, ent);
    }

    public void executeCommands() {
        var commands = networking.getCommands();
        commands.forEach(cmd -> cmd.execute(this));
    }

    public void updatePhysics(float delta) {
        var ents = entities.values();
        ents.forEach(ent -> ent.update(this, delta));

        ents.stream()
                .flatMap(a ->
                        ents.stream()
                                .filter(b -> a.eid < b.eid)
                                .map(b -> checkCollision(a, b))
                )
                .flatMap(Optional::stream)
                .forEach(CollisionManifold::resolve);
        ents.forEach(ent -> broadcast(new UpdateEntity(ent)));
    }

    public PhysicsEntity getEntity(int eid) {
        return entities.get(eid);
    }

    public void addEntity(PhysicsEntity ent) {
        entities.put(ent.eid, ent);
    }

    public void deleteEntity(int eid) {
        entities.remove(eid);
    }

    public Collection<PhysicsEntity> getEntities() {
        return entities.values();
    }

    public void send(int pid, ServerCommand command) {
        networking.send(pid, command);
    }

    public void broadcast(ServerCommand command) {
        networking.broadcast(command);
    }

}
