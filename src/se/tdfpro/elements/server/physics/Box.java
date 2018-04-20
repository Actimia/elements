package se.tdfpro.elements.server.physics;

import java.util.stream.Stream;

public class Box {

    private final Vec2 topLeft;
    private final Vec2 size;

    public Box(Vec2 topLeft, Vec2 size) {
        this.topLeft = topLeft;
        this.size = size;
    }

    public Box(float x, float y, float width, float height) {
        this(new Vec2(x, y), new Vec2(width, height));
    }

    public Vec2 center() { return topLeft.add(size.scale(0.5f)); }

    public float width() { return size.x; }

    public float height() { return size.y; }

    public Vec2 topLeft() {return topLeft;}

    public Vec2 topRight() {return topLeft.add(new Vec2(size.x, 0));}

    public Vec2 bottomLeft() {return topLeft.add(new Vec2(0, size.y));}

    public Vec2 bottomRight() {return topLeft.add(size);}

    public Stream<Vec2> corners() {
        return Stream.of(topLeft(), topRight(), bottomRight(), bottomLeft());
    }

    public Vec2 size() {
        return size;
    }
}
