package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.GameContainer;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.Vec2;

public abstract class DynamicEntity extends PhysicsEntity {
    protected static final float FRICTION_STOP = 1;

    protected final float invMass;
    protected Vec2 velocity;

    public DynamicEntity(Vec2 position, Vec2 velocity, float invMass, Material material) {
        super(position, material);
        this.velocity = velocity;
        this.invMass = invMass;
    }

    @Override
    public void physicsStep(float delta) {
        position = position.add(velocity.scale(delta));
        velocity = velocity.scale((float) Math.pow(material.getFriction(), delta));
        if (velocity.length2() < FRICTION_STOP) {
            velocity = Vec2.ZERO;
        }
    }

    @Override
    public void update(GameServer game, float delta) {

    }

    @Override
    public void update(GameClient game, float delta) {
        // do full physics step as interpolation
        physicsStep(delta);
    }

    @Override
    public Vec2 getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public void changeVelocity(Vec2 delta) {
        velocity = velocity.add(delta);
    }

    @Override
    public float getInvMass() {
        return invMass;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
