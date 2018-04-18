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

    private void doEulerStep(float delta) {
        position = position.add(velocity.scale(delta));
        velocity = velocity.scale((float) Math.pow(material.getFriction(), delta));
        if (velocity.length2() < FRICTION_STOP) {
            velocity = Vec2.ZERO;
        }
    }

    @Override
    public void updateServer(GameServer game, float delta){
        doEulerStep(delta);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {
        doEulerStep(delta);
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public void changeVelocity(Vec2 delta) {
        velocity = velocity.add(delta);
    }

    public float getInvMass() {
        return invMass;
    }

    public boolean isDynamic() {
        return true;
    }
}
