package se.tdfpro.elements.server.physics;

import org.newdawn.slick.Color;

public enum Materials implements Material {
    PLAYER(0.4f, 0.2f, Color.white),
    PROJECTILE(1f, 1f, Color.red),
    WALL(0.2f, 1.0f, Color.orange);

    private final float restitution;
    private final float friction;
    private final Color color;

    Materials(float rest, float fric, Color color) {
        restitution = rest;
        friction = fric;
        this.color = color;
    }

    @Override
    public float getRestitution() {
        return restitution;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
