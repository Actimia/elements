package se.tdfpro.elements.entity;

import org.newdawn.slick.Color;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Box;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PlayerEntity;
import se.tdfpro.elements.server.physics.entity.Ray;

import static se.tdfpro.elements.server.physics.CollisionManifold.checkCollision;

public class World extends Entity {

    @Override
    public Entity init(GameServer game) {
        var origin = new Vec2(0, 0);
        var area = new Vec2(1600, 1000);
        var playArea = new Box(origin, area);

        addChild(new Ray(playArea.topLeft(), Vec2.RIGHT).init(game));
        addChild(new Ray(playArea.topRight(), Vec2.DOWN).init(game));
        addChild(new Ray(playArea.bottomRight(), Vec2.LEFT).init(game));
        addChild(new Ray(playArea.bottomLeft(), Vec2.UP).init(game));

        addChild(new PlayerEntity(new Vec2(800, 600), Vec2.ZERO, -1, "", Color.white).init(game));
        addChild(new PlayerEntity(new Vec2(400, 600), new Vec2(20, 0), -1, "", Color.white).init(game));
        return super.init(game);
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {

    }

    @Override
    public void update(GameClient game, float delta) {
//        children.forEach(ent -> ent.physicsStep(delta));
//
//        // Collision detection and resolving
//        children.stream()
//            .flatMap(a -> children.stream()
//                // strict less than ensures entities are never checked against themselves
//                .filter(b -> a.getId() < b.getId())
//                .map(b -> checkCollision(a, b))
//            ).filter(Optional::isPresent)
//            .map(Optional::get)
//            .forEach(mani -> mani.resolve(this));
//
//        // Update clients
//        children.stream()
//            .filter(PhysicsEntity::isDynamic)
//            .forEach(ent -> broadcast(new UpdatePhysics(ent)));
    }

    @Override
    public void update(GameServer game, float delta) {

    }
}
