package se.tdfpro.elements.server.physics;

import java.util.stream.Stream;

public class Box {

    private final Vec2 topleft;
    private final Vec2 size;

    public Box(Vec2 topleft, Vec2 size) {

        this.topleft = topleft;
        this.size = size;
    }

    public Vec2 topLeft() {return topleft;}
    public Vec2 topRight() {return topleft.add(new Vec2(size.x, 0));}
    public Vec2 bottomLeft() {return topleft.add(new Vec2(0, size.y));}
    public Vec2 bottomRight() {return topleft.add(size);}

    public Stream<Vec2> corners() {
        return Stream.of(topLeft(), topRight(), bottomRight(), bottomLeft());
    }
}
