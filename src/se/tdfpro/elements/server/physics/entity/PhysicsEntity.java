package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.Vec2;

public abstract class PhysicsEntity extends Entity {

    protected final Material material;

    public PhysicsEntity(Vec2 position, Material material) {
        super(position);
        this.material = material;
    }

    @Override
    public Entity init(GameClient game) {
        return super.init(game);
    }

    @Override
    public Entity init(GameServer game) {
        game.addPhysicsEntity(this);
        return super.init(game);
    }

    @Override
    public void render(GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        g.rotate(0, 0, getVelocity().theta());
        g.setColor(getColor());

        draw(game, g);

        g.popTransform();
    }

    @Override
    public void destroy(GameServer game) {
        game.removePhysicsEntity(this);
    }

    public void physicsStep(float delta) {

    }

    public Color getColor() {
        return material.getColor();
    }

    public void onCollide(GameServer game, PhysicsEntity other) {

    }

    public Vec2 getVelocity() {
        return Vec2.ZERO;
    }

    public void setVelocity(Vec2 velocity) {
        // silently ignore for static objects.
    }

    public boolean isDynamic() {
        return false;
    }

    public float getInvMass() {
        return 0;
    }

    public Material getMaterial() {
        return material;
    }

    public void changeVelocity(Vec2 delta) {

    }

    public void changePosition(Vec2 delta) {
        position = position.add(delta);
    }



//    @Override
//    public abstract void encodeConstructorParams(Encoder encoder);
}
