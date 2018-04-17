package se.tdfpro.elements.server.engine;

public class PhysicsEntity {
    private static int nextid = 0;
    private Vec2 position;
    private Vec2 velocity;
    private float radius;
    private float restitution = 0.4f;
    private float mass = 70f;
    public final int id = nextid++;

    public PhysicsEntity(Vec2 pos, Vec2 velo, float radius) {
        position = pos;
        velocity = velo;
        this.radius = radius;
    }

    void update(GameServer game, float delta){
        position = position.add(velocity.scale(delta));


        for (PhysicsEntity ent : game.getEntities().values()) {
            var limit = radius + ent.radius;
            limit *= limit; // squared to avoid sqrt
            var normal = position.sub(ent.position);
            if (normal.length2() < limit) {
                resolveCollision(ent, normal);
            }
        }
    }

    private void resolveCollision(PhysicsEntity other, Vec2 normal) {
        normal = normal.norm();
        var velocity_along_normal = (other.velocity.sub(velocity)).dot(normal);
        if(velocity_along_normal > 0) {
            // objects are moving apart
            return;
        }
        var e = Math.min(restitution, other.restitution);
        var j = -(1+e) * velocity_along_normal;
        var invMass = 1/mass;
        var invMassOther = 1/other.mass;
        j /= invMass + invMassOther;

        var impulse = normal.scale(j);
        velocity = velocity.sub(impulse.scale(invMass));
        other.velocity = other.velocity.sub(impulse.scale(invMassOther));

        var slop = 1f;
        var depth = position.sub(other.position).length() - (radius + other.radius);
        if(depth > slop) {
            var pos_correction = normal.scale(0.5f * depth / (invMass + invMassOther));
            position = position.sub(pos_correction.scale(invMass));
            other.position = other.position.sub(pos_correction.scale(invMassOther));
        }

    }
}
