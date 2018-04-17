package se.tdfpro.elements.server.engine;


import org.newdawn.slick.geom.Vector2f;

import static se.tdfpro.elements.client.engine.Camera.TO_DEGREES;

public class Vec2  {
    public static final Vec2 ZERO = new Vec2(0,0);
    public final float x;
    public final float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vector2f v2f) {
        this(v2f.x, v2f.y);
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

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public Vector2f toVector2f() {
        return new Vector2f(x,y);
    }
}
