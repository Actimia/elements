package se.tdfpro.elements.server.physics;

import java.util.stream.Stream;

public class Box {

    private final Vec2 topLeft;
    private final Vec2 size;

    public Box(Vec2 topLeft, Vec2 size) {

        this.topLeft = topLeft;
        this.size = size;
    }

    public Vec2 topLeft() {return topLeft;}

    public Vec2 topRight() {return topLeft.add(new Vec2(size.x, 0));}

    public Vec2 bottomLeft() {return topLeft.add(new Vec2(0, size.y));}

    public Vec2 bottomRight() {return topLeft.add(size);}

    public Stream<Vec2> corners() {
        return Stream.of(topLeft(), topRight(), bottomRight(), bottomLeft());
    }
}
