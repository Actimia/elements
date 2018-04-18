package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.physics.shapes.Circle;
import se.tdfpro.elements.server.physics.shapes.Ray;

import java.util.Optional;

public class CollisionManifold {
    public PhysicsEntity2 a;
    public PhysicsEntity2 b;
    public Vec2 normal;
    public float depth;

    public CollisionManifold(PhysicsEntity2 a, PhysicsEntity2 b, Vec2 normal, float depth) {
        this.a = a;
        this.b = b;
        this.normal = normal.norm();
        this.depth = depth;
    }

    private void resolve() {
        var velocity_along_normal = (b.velocity.sub(a.velocity)).dot(normal);

        if (velocity_along_normal >= 0) {
            var e = Math.min(a.material.getRestitution(), b.material.getRestitution());
            var j = -(1 + e) * velocity_along_normal;
            j /= a.invMass + b.invMass;

            var impulse = normal.scale(j);
            a.velocity = a.velocity.sub(impulse.scale(a.invMass));
            b.velocity = b.velocity.add(impulse.scale(b.invMass));
        }

        var slop = 0f;
        if (depth > slop) {
            var pos_correction = normal.scale(0.8f * depth / (a.invMass + b.invMass));
            a.position = a.position.sub(pos_correction.scale(a.invMass));
            b.position = b.position.sub(pos_correction.scale(b.invMass));
        }
    }

    public static Optional<CollisionManifold> checkCollision(Circle a, Circle b) {
        var limit = a.radius + b.radius;
        var normal = b.position.sub(a.position);
        if (normal.length2() <= limit * limit) {
            var depth = normal.length() - limit;
            return Optional.of(new CollisionManifold(a, b, normal, depth));
        }
        return Optional.empty();
    }

    public static Optional<CollisionManifold> checkCollision(Circle a, Ray b) {
        var t = a.position.sub(b.position).dot(b.direction);
        var closest = b.position.add(b.direction.scale(t));

        var limit = a.radius;
        var normal = closest.sub(a.position);
        if (normal.length2() <= limit * limit) {
            var depth = normal.length() - limit;
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

    public static Optional<CollisionManifold> checkCollision(PhysicsEntity2 a, PhysicsEntity2 b) {
        throw new RuntimeException("Unknown entity collision type: " + a.getClass().getSimpleName() + " and " + b.getClass().getSimpleName());
    }
}
