package se.tdfpro.elements.entity.physics;

import org.newdawn.slick.Color;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.command.server.UpdatePhysics;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Box;
import se.tdfpro.elements.util.Vec2;

import java.util.Optional;
import java.util.stream.Collectors;

import static se.tdfpro.elements.entity.physics.CollisionManifold.checkCollision;

public class World extends Entity {

    @Override
    public void init(GameServer game) {
        var origin = new Vec2(0, 0);
        var area = new Vec2(1600, 1000);
        var playArea = new Box(origin, area);


        var id = getId();

        game.createEntity(id, new Ray(playArea.topLeft(), Vec2.RIGHT));
        game.createEntity(id, new Ray(playArea.topRight(), Vec2.DOWN));
        game.createEntity(id, new Ray(playArea.bottomRight(), Vec2.LEFT));
        game.createEntity(id, new Ray(playArea.bottomLeft(), Vec2.UP));

        game.createEntity(id, new PlayerEntity(new Vec2(800, 600), Vec2.ZERO, -1, "", Color.white));
        game.createEntity(id, new PlayerEntity(new Vec2(400, 600), new Vec2(20, 0), -1, "", Color.white));
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {

    }

    @Override
    public void update(GameServer game, float delta) {
        // copy the list as not to get ConcurrentModificationException
        var physics = children.stream().filter(c -> c instanceof PhysicsEntity).map(c -> (PhysicsEntity) c).collect(Collectors.toList());
        physics.forEach(ent -> ent.physicsStep(delta));

        // Collision detection and resolving
        physics.stream()
            .flatMap(a -> physics.stream()
                // strict less than ensures entities are never checked against themselves
                .filter(b -> a.getId() < b.getId())
                .map(b -> checkCollision(a, b))
            ).filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(mani -> mani.resolve(game));

        // Update clients
        physics.stream()
            .filter(PhysicsEntity::isDynamic)
            .forEach(ent -> game.broadcast(new UpdatePhysics(ent)));
    }
}
