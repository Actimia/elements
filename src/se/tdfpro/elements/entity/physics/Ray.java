package se.tdfpro.elements.entity.physics;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.DecodeConstructor;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Vec2;

public class Ray extends PhysicsEntity {

    private final Vec2 direction;

    @DecodeConstructor
    public Ray(Vec2 position, Vec2 dir) {
        super(position, Material.WALL);
        this.direction = dir.norm();
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {
        encoder.encode(position);
        // direction is not what is passed in, but .norm() is idempotent
        encoder.encode(direction);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        g.setLineWidth(4);
        g.setColor(getMaterial().getColor());
        var dst = direction.scale(2000f);
        var src = dst.invert();
        g.drawLine(src.x, src.y, dst.x, dst.y);
    }

    @Override
    public void update(GameClient game, float delta) {

    }

    @Override
    public void update(GameServer game, float delta) {

    }

    @Override
    public String toString() {
        return String.format("Ray [%s->%s]", position, direction);
    }

    public Vec2 getDirection() {
        return direction;
    }

    public float closestDistance(Vec2 point) {
        return point.sub(position).dot(direction.perpendicular());
    }
}
