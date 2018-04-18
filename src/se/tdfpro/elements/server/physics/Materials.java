package se.tdfpro.elements.server.physics;

public enum Materials implements Material{
    PLAYER(0.4f, 0.8f),
    WALL(0.2f, 1.0f);

    private final float restitution;
    private final float friction;

    Materials(float rest, float fric) {
        restitution = rest;
        friction = fric;
    }
    @Override
    public float getRestitution() {
        return restitution;
    }

    @Override
    public float getFriction() {
        return friction;
    }
}
