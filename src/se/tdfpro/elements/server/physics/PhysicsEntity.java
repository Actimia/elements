package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.command.server.UpdateEntity;

public class PhysicsEntity {
    private static int nextid = 0;
    public Vec2 position;
    public Vec2 velocity;
    public Vec2 impulse = Vec2.ZERO;
    private float radius;
    private float restitution = 0.4f;
    private float invMass = 1/10f;
    public final int id = nextid++;

    public PhysicsEntity(Vec2 pos, Vec2 velo, float radius) {
        position = pos;
        velocity = velo;
        this.radius = radius;
    }

    public void update(GameServer game, float delta){
        velocity = velocity.add(impulse);
        impulse = Vec2.ZERO;
        position = position.add(velocity.scale(delta));
        velocity = velocity.scale(0.95f);

        for (PhysicsEntity ent : game.getEntities().values()) {
            if(ent.id != this.id) {
                var limit = radius + ent.radius;
                var normal = position.sub(ent.position);
                if (normal.length2() < limit * limit) {
                    resolveCollision(ent, normal);
                }
            }
        }
        game.broadcast(new UpdateEntity(this));
    }

    private void resolveCollision(PhysicsEntity other, Vec2 normal) {
        normal = normal.norm();
        var velocity_along_normal = (other.velocity.sub(velocity)).dot(normal);


        if(velocity_along_normal >= 0) {
            var e = Math.min(restitution, other.restitution);
            var j = -(1+e) * velocity_along_normal;
            j /= invMass + other.invMass;

            var impulse = normal.scale(j);
            velocity = velocity.sub(impulse.scale(invMass));
            other.velocity = other.velocity.add(impulse.scale(other.invMass));
        }

        var slop = 0f;
        var depth = position.sub(other.position).length() - (radius + other.radius);
        if(depth > slop) {
            var pos_correction = normal.scale(0.8f * depth / (invMass + other.invMass));
            position = position.sub(pos_correction.scale(invMass));
            other.position = other.position.sub(pos_correction.scale(other.invMass));
        }

    }
}
