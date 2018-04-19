package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.entity.Circle;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Ray;

import java.util.Optional;

public class CollisionManifold {
    private static final float slop = -1f;
    private static final float corrFactor = 0.8f;
    public final PhysicsEntity a;
    public final PhysicsEntity b;
    public final Vec2 normal;
    public final float depth;

    public CollisionManifold(PhysicsEntity a, PhysicsEntity b, Vec2 normal, float depth) {
        this.a = a;
        this.b = b;
        if (normal.equals(Vec2.ZERO)) normal = Vec2.RIGHT;
        this.normal = normal.norm();
        this.depth = depth;
    }

    public void resolve(GameServer game) {
        var velocity_along_normal = b.getVelocity().sub(a.getVelocity()).dot(normal);

        if (velocity_along_normal <= 0) {
            var e = Math.max(a.getMaterial().getRestitution(), b.getMaterial().getRestitution());
            var j = -(1 + e) * velocity_along_normal;
            j /= a.getInvMass() + b.getInvMass();

            var impulse = normal.scale(j);
            a.changeVelocity(impulse.scale(-a.getInvMass()));
            b.changeVelocity(impulse.scale(b.getInvMass()));
        }

        if (depth < slop) {
            var pos_correction = normal.scale(corrFactor * depth / (a.getInvMass() + b.getInvMass()));
            a.changePosition(pos_correction.scale(a.getInvMass()));
            b.changePosition(pos_correction.scale(-b.getInvMass()));
        }
        a.onCollide(game, b);
        b.onCollide(game, a);
    }

    public static Optional<CollisionManifold> checkCollision(Circle a, Circle b) {
        var limit = a.radius + b.radius;
        var normal = b.getPosition().sub(a.getPosition());
        if (normal.length2() <= limit * limit) {
            var depth = normal.length() - limit;

            if (normal.equals(Vec2.ZERO)) {
                normal = Vec2.RIGHT;
            }
            return Optional.of(new CollisionManifold(a, b, normal, depth));
        }
        return Optional.empty();
    }

    public static Optional<CollisionManifold> checkCollision(Circle a, Ray b) {
        var limit = a.radius;
        var normal = b.getDirection().perpendicular();
        var dist = b.closestDistance(a.getPosition());
        if (dist < limit) {
            // beyond the wall
            return Optional.of(new CollisionManifold(a, b, normal.scale(Math.abs(dist)), dist - limit));
        }
//        var normal = perp.scale(dist);
//        if (normal.length2() < limit * limit) {
//            var depth = normal.length() - limit;
//            return Optional.of(new CollisionManifold(a, b, normal.project(perp), depth));
//        }

        return Optional.empty();
    }

    public static Optional<CollisionManifold> checkCollision(Ray a, Circle b) {
        return checkCollision(b, a).map(cmf -> new CollisionManifold(cmf.a, cmf.b, cmf.normal.invert(), cmf.depth));
    }

    public static Optional<CollisionManifold> checkCollision(Ray a, Ray b) {
        // Rays can never collide with each other
        return Optional.empty();
    }

    public static Optional<CollisionManifold> checkCollision(PhysicsEntity a, PhysicsEntity b) {
        if (a instanceof Circle && b instanceof Circle) return checkCollision((Circle) a, (Circle) b);
        if (a instanceof Ray && b instanceof Circle) return checkCollision((Ray) a, (Circle) b);
        if (a instanceof Circle && b instanceof Ray) return checkCollision((Circle) a, (Ray) b);
        if (a instanceof Ray && b instanceof Ray) return checkCollision((Ray) a, (Ray) b);

        throw new RuntimeException("Unknown entity collision type: "
                + a.getClass().getSimpleName() + " and "
                + b.getClass().getSimpleName());
    }
}
