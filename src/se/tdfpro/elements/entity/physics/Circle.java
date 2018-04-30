package se.tdfpro.elements.entity.physics;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.util.Vec2;

public abstract class Circle extends DynamicEntity {

    public final float radius;

    public Circle(Vec2 position, Vec2 velocity, float invMass, Material material, float radius) {
        super(position, velocity, invMass, material);

        this.radius = radius;
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        g.setLineWidth(3);
        g.drawOval(-radius, -radius, 2 * radius, 2 * radius);
        g.drawLine(0, 0, radius, 0);
    }

    @Override
    public String toString() {
        return String.format("Circle [%s]", position);
    }
}
