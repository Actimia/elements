package se.tdfpro.elements.server;

import org.newdawn.slick.Color;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.command.server.CreateEntity;
import se.tdfpro.elements.command.server.DestroyEntity;
import se.tdfpro.elements.command.server.PlayerDisconnect;
import se.tdfpro.elements.command.server.UpdatePhysics;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.entity.physics.Ray;
import se.tdfpro.elements.entity.physics.World;
import se.tdfpro.elements.net.Server;
import se.tdfpro.elements.entity.physics.PhysicsEntity;
import se.tdfpro.elements.entity.physics.PlayerEntity;
import se.tdfpro.elements.util.Box;
import se.tdfpro.elements.util.Vec2;

import java.util.*;

import static java.lang.Thread.sleep;
import static se.tdfpro.elements.entity.physics.CollisionManifold.checkCollision;

public class GameServer {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000 / TICKS;

    private long lastTickStart = System.currentTimeMillis();

    private int nextId = 1;

    private final Server networking;
    private final Map<Integer, Entity> entities = new HashMap<>();
    private final Map<Integer, PhysicsEntity> physicsEntities = new HashMap<>();

    private final World world = new World();

    public GameServer(Server net) {
        this.networking = net;


        world.setId(0);
        entities.put(world.getId(), world);
        world.init(this);
    }

    public void run() {
        while (true) {
            var delta = (System.currentTimeMillis() - lastTickStart) / 1000f;
            lastTickStart = System.currentTimeMillis();

            executeCommands();
            world.update(this, delta);
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

    public Entity createEntity(int parent, Entity entity) {
        int id = getNextId();
        entity.setId(id);
        entities.put(id, entity);
        entities.get(parent).addChild(entity);
        entity.init(this);
        if (entity instanceof PhysicsEntity) {
            physicsEntities.put(id, (PhysicsEntity) entity);
        }
        broadcast(new CreateEntity(entity));
        return entity;
    }


    public Entity destroyEntity(Entity entity) {
        var id = entity.getId();
        entity.destroy(this);
        entities.remove(id);
        entities.get(entity.getParent()).removeChild(entity);
        if (entity instanceof PhysicsEntity) {
            physicsEntities.remove(id);
        }
        broadcast(new DestroyEntity(entity));
        return entity;
    }

    private void executeCommands() {
        var commands = networking.getCommands();
        commands.forEach(cmd -> cmd.execute(this));
    }

    private void updatePhysics(float delta) {
        var entities = new ArrayList<>(this.physicsEntities.values());
        entities.forEach(ent -> ent.physicsStep(delta));

        // Collision detection and resolving
        entities.stream()
            .flatMap(a -> entities.stream()
                // strict less than ensures entities are never checked against themselves
                .filter(b -> a.getId() < b.getId())
                .map(b -> checkCollision(a, b))
            ).filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(mani -> mani.resolve(this));

        // Update clients
        entities.stream()
            .filter(PhysicsEntity::isDynamic)
            .forEach(ent -> broadcast(new UpdatePhysics(ent)));
    }

    public Entity getEntity(int eid) {
        return entities.get(eid);
    }


    public int getNextId() {
        return nextId++;
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public void send(int pid, ServerCommand command) {
        networking.send(pid, command);
    }

    public void broadcast(ServerCommand command) {
        networking.broadcast(command);
    }

    public void onDisconnect(int pid) {
        getEntities().stream()
            .filter(ent -> ent instanceof PlayerEntity)
            .map(ent -> (PlayerEntity) ent)
            .filter(p -> p.getController() == pid)
            .findFirst()
            .ifPresent(player -> {
                var dc = new PlayerDisconnect();
                dc.pid = pid;
                broadcast(dc);
            });
    }


}
