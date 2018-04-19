package se.tdfpro.elements.server.physics;

import org.newdawn.slick.geom.Vector2f;

import static se.tdfpro.elements.client.Camera.TO_DEGREES;

public class Vec2 {
    private static final float TOLERANCE = 1e-4f;
    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 UP = new Vec2(0, -1);
    public static final Vec2 DOWN = new Vec2(0, 1);
    public static final Vec2 LEFT = new Vec2(-1, 0);
    public static final Vec2 RIGHT = new Vec2(1, 0);
    public final float x;
    public final float y;

    public Vec2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(final Vector2f v2f) {
        this(v2f.x, v2f.y);
    }

    public Vec2 add(final Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }

    public Vec2 sub(final Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }

    public Vec2 norm() {
        var len = length();
        return new Vec2(x / len, y / len);
    }

    public Vec2 rotate(final float theta) {
        final var cos = (float) Math.cos(theta);
        final var sin = (float) Math.sin(theta);
        return new Vec2(x * cos - y * sin, x * sin + y * cos);
    }

    public Vec2 scale(final float delta) {
        return new Vec2(x * delta, y * delta);
    }

    public Vec2 invert() {
        return new Vec2(-x, -y);
    }

    public Vec2 project(Vec2 o) {
        return scale(dot(o.norm()));
    }

    public float length() {
        return (float) Math.sqrt(length2());
    }

    public float length2() {
        return dot(this);
    }

    public float dot(final Vec2 o) {
        return x * o.x + y * o.y;
    }

    public float theta() {
        return (float) Math.atan2(y, x) * TO_DEGREES;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vec2)) return false;
        var o = (Vec2) obj;
        return (Math.abs(x - o.x) < TOLERANCE) && (Math.abs(y - o.y) < TOLERANCE);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public Vector2f toVector2f() {
        return new Vector2f(x, y);
    }

    public Vec2 perpendicular() {
        //noinspection SuspiciousNameCombination
        return new Vec2(-y, x);
    }
}
