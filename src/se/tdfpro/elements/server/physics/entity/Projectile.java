package se.tdfpro.elements.server.physics.entity;

import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.CreateProjectile;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Projectile extends Circle {
    public Projectile(Vec2 position, Vec2 velocity) {
        super(position, velocity, 0.2f, Materials.PROJECTILE, 10f);
    }

    @Override
    public ServerCommand makeCreateCommand() {
        return new CreateProjectile(this);
    }
}
