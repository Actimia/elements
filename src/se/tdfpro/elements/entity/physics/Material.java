package se.tdfpro.elements.entity.physics;

import org.newdawn.slick.Color;

public enum Material {
    PLAYER(0.4f, 0.2f, Color.white),
    PROJECTILE(1f, 1f, Color.red),
    WALL(0.2f, 1.0f, Color.orange);

    private final float restitution;
    private final float friction;
    private final Color color;

    Material(float restitution, float friction, Color color) {
        this.restitution = restitution;
        this.friction = friction;
        this.color = color;
    }

    public float getRestitution() {
        return restitution;
    }

    public float getFriction() {
        return friction;
    }

    public Color getColor() {
        return color;
    }
}
