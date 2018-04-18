package se.tdfpro.elements.server.physics;


import org.newdawn.slick.geom.Vector2f;

import static se.tdfpro.elements.client.engine.Camera.TO_DEGREES;

public class Vec2  {
    public static final Vec2 ZERO = new Vec2(0,0);
    public static final Vec2 UP = new Vec2(0,1);
    public static final Vec2 DOWN = new Vec2(0,-1);
    public static final Vec2 LEFT = new Vec2(-1,0);
    public static final Vec2 RIGHT = new Vec2(1,0);
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

    public Vec2 scale(float delta) {
        return new Vec2(x * delta, y * delta);
    }

    public Vec2 invert() {
        return new Vec2(-x, -y);
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

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public Vector2f toVector2f() {
        return new Vector2f(x,y);
    }
}
