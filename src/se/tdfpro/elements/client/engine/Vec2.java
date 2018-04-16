package se.tdfpro.elements.client.engine;

import org.newdawn.slick.geom.Vector2f;

import static se.tdfpro.elements.client.engine.Camera.TO_DEGREES;

public class Vec2 extends Vector2f {
    public Vec2() {
        super(0,0);
    }

    public Vec2(float x, float y) {
        super(x,y);
    }

    public Vec2 add(Vec2 o) {
        x += o.x;
        y += o.y;
        return this;
    }

    public Vec2 sub(Vec2 o) {
        x -= o.x;
        y -= o.y;
        return this;
    }

    public float theta() {
        return (float) Math.atan2(y,x) * TO_DEGREES;
    }
}
