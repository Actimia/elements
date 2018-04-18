package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.UpdateEntity;
import se.tdfpro.elements.server.physics.CollisionManifold;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.shapes.Ray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        var edge = new Ray(new Vec2(-500,-300), Vec2.UP);
        entities.put(edge.eid, edge);
    }

    public void run() {
        while (true) {

            var delta = (System.currentTimeMillis() - lastTickStart) / 1000f;
            lastTickStart = System.currentTimeMillis();

            executeCommands();
            updatePhysics(delta);

            var frametime = System.currentTimeMillis() - lastTickStart;
            var idletime = TICK_TIME - frametime;
            if (idletime < 0) idletime = 0;
            try {
                sleep(idletime);
            } catch (InterruptedException ignored) {
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

    public Map<Integer, PhysicsEntity> getEntities() {
        return entities;
    }

    public void send(int pid, ServerCommand command) {
        networking.send(pid, command);
    }

    public void broadcast(ServerCommand command) {
        networking.broadcast(command);
    }

}
