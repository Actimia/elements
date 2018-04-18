package se.tdfpro.elements.server.physics.shapes;

import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

public class Circle extends PhysicsEntity {

    public final float radius;

    public Circle(Vec2 position, Vec2 velocity, float invMass, Material material, float radius) {
        super(position, velocity, invMass, material);

        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Circle [%s]", position);
    }
}
