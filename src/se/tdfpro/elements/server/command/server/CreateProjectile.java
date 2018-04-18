package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Projectile;

public class CreateProjectile extends ServerCommand {
    @Send
    public int eid;
    @Send
    public Vec2 position;

    @Send
    public Vec2 velocity;

    public CreateProjectile() {}

    public CreateProjectile(Projectile projectile) {
        this.eid = projectile.getEid();
        this.position = projectile.getPosition();
        this.velocity = projectile.getVelocity();
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreateProjectile(" + eid + ")");
        var proj = new Projectile(position, velocity);
        proj.setEid(eid);
        game.addEntity(proj);
    }

}
