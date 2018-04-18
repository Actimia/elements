package se.tdfpro.elements.server.physics.shapes;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

public class Ray extends PhysicsEntity {

    public final Vec2 direction;

    public Ray(Vec2 position, Vec2 dir) {
        super(position, Vec2.ZERO, 0, Materials.WALL);
        this.direction = dir.norm();
    }

    @Override
    public void update(GameServer game, float delta) {
        // static object
    }

    @Override
    public String toString() {
        return String.format("Ray [%s->%s]", position, direction);
    }
}
