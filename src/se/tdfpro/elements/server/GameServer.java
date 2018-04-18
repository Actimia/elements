package se.tdfpro.elements.server;

import se.tdfpro.elements.net.Server;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.net.command.server.DeleteEntity;
import se.tdfpro.elements.net.command.server.UpdateEntity;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Circle;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Ray;

import java.util.*;

import static java.lang.Thread.sleep;
import static se.tdfpro.elements.server.physics.CollisionManifold.checkCollision;

public class GameServer {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000 / TICKS;

    private long lastTickStart = System.currentTimeMillis();

    private Server networking;
    private Map<Integer, PhysicsEntity> entities = new HashMap<>();

    public GameServer(Server net) {
        this.networking = net;

        var origin = new Vec2(0, 0);
        var area = new Vec2(1600, 1000);

        var top = new Ray(new Vec2(origin.x, origin.y), Vec2.RIGHT);
        var bottom = new Ray(new Vec2(origin.x + area.x, origin.y + area.y), Vec2.LEFT);
        var left = new Ray(new Vec2(origin.x, origin.y + area.y), Vec2.UP);
        var right = new Ray(new Vec2(origin.x + area.x, origin.y), Vec2.DOWN);

        spawnEntity(top);
        spawnEntity(bottom);
        spawnEntity(left);
        spawnEntity(right);

        spawnEntity(new Circle(new Vec2(800, 600), Vec2.ZERO, 2, Materials.PLAYER, 30f));
        spawnEntity(new Circle(new Vec2(400, 600), new Vec2(20, 0), 2, Materials.PLAYER, 30f));
    }

    public void run() {
        while (true) {
            var delta = (System.currentTimeMillis() - lastTickStart) / 1000f;
            lastTickStart = System.currentTimeMillis();

            executeCommands();
            updatePhysics(delta);
            var frameTime = System.currentTimeMillis() - lastTickStart;
            if (frameTime > TICK_TIME) {
                System.out.println("Very long frame warning: " + frameTime + "ms");
            } else if (frameTime > TICK_TIME / 2) {
                System.out.println("Long frame warning: " + frameTime + "ms");
            }
            var idleTime = TICK_TIME - frameTime;
            if (idleTime > 0) {
                try {
                    sleep(idleTime);
                } catch (InterruptedException ignored) {
                }
            }

        }
    }

    public void spawnEntity(PhysicsEntity ent) {
        entities.put(ent.getEid(), ent);
        broadcast(ent.makeCreateCommand());
    }

    public void executeCommands() {
        var commands = networking.getCommands();
        commands.forEach(cmd -> cmd.execute(this));
    }

    public void updatePhysics(float delta) {
        var ents = new ArrayList<>(entities.values());
        ents.forEach(ent -> ent.updateServer(this, delta));

        // Collision detection and resolving
        ents.stream()
                .flatMap(a ->
                        ents.stream()
                                .filter(b -> a.getEid() < b.getEid())
                                .map(b -> checkCollision(a, b))
                )
                .flatMap(Optional::stream)
                .forEach(mani -> mani.resolve(this));

        // Update clients
        ents.stream()
                .filter(PhysicsEntity::isDynamic)
                .forEach(ent -> broadcast(new UpdateEntity(ent)));
    }

    public PhysicsEntity getEntity(int eid) {
        return entities.get(eid);
    }

    public void deleteEntity(int eid) {
        entities.remove(eid);
        broadcast(new DeleteEntity(eid));
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
