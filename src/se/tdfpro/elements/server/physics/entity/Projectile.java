package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.CreateProjectile;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Projectile extends Circle {
    private int bounces = 5;
    private PhysicsEntity source;

    public Projectile(Vec2 position, Vec2 velocity, PhysicsEntity source) {
        super(position, velocity, 1f, Materials.PROJECTILE, 10f);
        this.source = source;
    }

    @Override
    public ServerCommand makeCreateCommand() {
        var res = new CreateProjectile();
        res.position = position;
        res.velocity = velocity;
        res.eid = getEid();
        res.sourceid = source.getEid();
        return res;
    }

    @Override
    public void draw(Graphics g) {
        g.fillOval(-radius, -radius, 2 * radius, 2 * radius);
    }

    @Override
    public void onCollide(GameServer game, PhysicsEntity other) {
        if (--bounces == 0) {
            game.deleteEntity(getEid());
        }
    }
}
