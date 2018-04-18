package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.GameServer;

public abstract class PhysicsEntity {
    private static final float FRICTION_STOP = 1;
    private static int nextEid;

    public final int eid = nextEid++;
    public Vec2 position;
    public Vec2 velocity;

    public final float invMass;
    public final Material material;

    public PhysicsEntity(Vec2 position, Vec2 velocity, float invMass, Material material) {
        this.position = position;
        this.velocity = velocity;
        this.invMass = invMass;
        this.material = material;
    }

    public void update(GameServer game, float delta){
        position = position.add(velocity.scale(delta));
        velocity = velocity.scale((float) Math.pow(material.getFriction(), delta));
        if (velocity.length2() < FRICTION_STOP) {
            velocity = Vec2.ZERO;
        }
    }



}
