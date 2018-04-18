package se.tdfpro.elements.server.physics.shapes;

import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.PhysicsEntity2;
import se.tdfpro.elements.server.physics.Vec2;

public class Ray extends PhysicsEntity2 {

    public final Vec2 direction;

    public Ray(Vec2 position, Vec2 dir) {
        super(position, Vec2.ZERO, 0, Materials.WALL);
        this.direction = dir.norm();
    }
}
