package se.tdfpro.elements.client.engine;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class Camera {
    private static final Vector2f viewport = new Vector2f(1600, 1000);

    private Vector2f translate = viewport.copy().scale(0.5f);
    private float scale = 1;
    private float rotation = 0;

    public static final float TO_DEGREES = (float) (360 / (2*Math.PI));
    public static final float TO_RADIANS = (float) 1/TO_DEGREES;

    public void project(Graphics g) {
        g.translate(translate.x, translate.y);
        g.rotate(0, 0, rotation);
        g.scale(scale, scale);
    }

    public Vector2f project(Vector2f world) {
        var res = world.copy();
        res.add(translate);
        res.add(rotation);
        res.scale(scale);
        return res;
    }

    public Vector2f unproject(Vector2f camera) {
        var res = camera.copy();
        res.scale(1/scale);
        res.add(-rotation);
        res.add(translate.negate());
        return res;
    }


    public void translate(float x, float y) {
        translate(new Vector2f(x,y));
    }

    private void translate(Vector2f offset) {
        translate.add(offset);
    }

    public Vector2f getTranslation() {
        return translate;
    }

    public void setTranslation(Vector2f translate) {
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

    public float modRotation(float degs) {
        return this.rotation += degs;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float degs) {
        this.rotation = degs;
    }

}
