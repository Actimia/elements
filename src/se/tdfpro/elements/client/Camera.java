package se.tdfpro.elements.client;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.server.physics.Vec2;

public class Camera {
    private static final Vec2 viewport = new Vec2(1600, 1000);
    private static final Vec2 cameraOffset = new Vec2(-800, -500);

    private Vec2 translate = Vec2.ZERO;
    private float scale = 1;
    private float rotation = 0;

    public static final float TO_DEGREES = (float) (360 / (2 * Math.PI));
    public static final float TO_RADIANS = 1f / TO_DEGREES;

    public void project(Graphics g) {
        g.translate(translate.x, translate.y);
        g.rotate(0, 0, rotation);
        g.scale(scale, scale);
    }

    public Vec2 project(Vec2 world) {

        return world.add(translate).rotate(rotation).scale(scale);
    }

    public Vec2 unproject(Vec2 camera) {
        return camera.scale(1 / scale).rotate(-rotation).add(translate.invert());
    }

    public void centerOn(Vec2 target) {
        translate = target.invert().sub(cameraOffset);
    }

    public void translate(float x, float y) {
        translate(new Vec2(x, y));
    }

    private void translate(Vec2 offset) {
        translate.add(offset);
    }

    public Vec2 getTranslation() {
        return translate;
    }

    public void setTranslation(Vec2 translate) {
        this.translate = translate;
    }

    private float modScale(float scale) {
        return this.scale += scale;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float modRotation(float degrees) {
        return this.rotation += degrees;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float degrees) {
        this.rotation = degrees;
    }

}
