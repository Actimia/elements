package se.tdfpro.elements.server.engine;


import static se.tdfpro.elements.client.engine.Camera.TO_DEGREES;

public class Vec2  {
    private final float x;
    private final float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }

    public Vec2 sub(Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }

    public Vec2 norm() {
        var len = length();
        return new Vec2(x / len, y / len);
    }

    public float length() {
        return (float) Math.sqrt(length2());
    }
    public float length2() {
        return dot(this);
    }
    public float dot(Vec2 o) {
        return x*o.x + y*o.y;
    }

    public float theta() {
        return (float) Math.atan2(y,x) * TO_DEGREES;
    }

    public Vec2 scale(float delta) {
        return new Vec2(x * delta, y * delta);
    }
}
