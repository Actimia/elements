package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.physics.shapes.Circle;
import se.tdfpro.elements.server.physics.shapes.Ray;

import java.util.Optional;

public class CollisionManifold {
    private static final float slop = -1f;
    private static final float corrFactor = 1f;
    public PhysicsEntity a;
    public PhysicsEntity b;
    public Vec2 normal;
    public float depth;

    public CollisionManifold(PhysicsEntity a, PhysicsEntity b, Vec2 normal, float depth) {
        this.a = a;
        this.b = b;
        if (normal.equals(Vec2.ZERO)) normal = Vec2.RIGHT;
        this.normal = normal.norm();
        this.depth = depth;
    }

    public void resolve() {
        var velocity_along_normal = b.velocity.sub(a.velocity).dot(normal);

        if (velocity_along_normal <= 0) {
            var e = Math.min(a.material.getRestitution(), b.material.getRestitution());
            var j = -(1 + e) * velocity_along_normal;
            j /= a.invMass + b.invMass;

            var impulse = normal.scale(j);
            a.velocity = a.velocity.sub(impulse.scale(a.invMass));
            b.velocity = b.velocity.add(impulse.scale(b.invMass));
        }

        if (depth < slop) {
            var pos_correction = normal.scale(corrFactor * depth / (a.invMass + b.invMass));
            a.position = a.position.add(pos_correction.scale(a.invMass));
            b.position = b.position.sub(pos_correction.scale(b.invMass));
        }
    }

    public static Optional<CollisionManifold> checkCollision(Circle a, Circle b) {
        var limit = a.radius + b.radius;
        var normal = b.position.sub(a.position);
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
        var t = a.position.sub(b.position).dot(b.direction);
        var closest = b.position.add(b.direction.scale(t));

        var limit = a.radius;
        var normal = a.position.sub(closest);
        if (normal.length2() <= limit * limit) {
            var depth = normal.length() - limit;

            if (normal.equals(Vec2.ZERO)) {
                normal = b.direction.perpendicular();
            }
            return Optional.of(new CollisionManifold(a, b, normal, depth));
        }

        return Optional.empty();
    }

    public static Optional<CollisionManifold> checkCollision(Ray a, Circle b) {
        return checkCollision(b, a).map(m -> {
            m.normal = m.normal.invert();
            return m;
        });
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
