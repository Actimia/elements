package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.command.server.CreateCircle;
import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.Vec2;

public abstract class Circle extends DynamicEntity {

    public final float radius;

    public Circle(Vec2 position, Vec2 velocity, float invMass, Material material, float radius) {
        super(position, velocity, invMass, material);

        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        g.setLineWidth(3);
        g.drawOval(-radius, -radius, 2 * radius, 2 * radius);
        g.drawLine(0, 0, radius, 0);
    }

    @Override
    public String toString() {
        return String.format("Circle [%s]", position);
    }

    @Override
    public ServerCommand makeCreateCommand() {
        return new CreateCircle(this);
    }
}
