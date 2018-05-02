package se.tdfpro.elements.entity.physics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Vec2;

public abstract class PhysicsEntity extends Entity {
    protected Vec2 position;
    protected final Material material;

    public PhysicsEntity(Vec2 position, Material material) {
        this.position = position;
        this.material = material;
    }

    @Override
    public void render(GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        g.rotate(0, 0, getVelocity().theta());
        g.setColor(getColor());

        draw(game, g);
        children.forEach(c -> c.render(game, g));
        g.popTransform();
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

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

}
