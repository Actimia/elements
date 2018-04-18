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
    public int sourceid;

    @Send
    public Vec2 position;

    @Send
    public Vec2 velocity;

    public CreateProjectile() {
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreateProjectile(" + eid + ")");
        var proj = new Projectile(position, velocity, game.getEntity(sourceid));
        proj.setEid(eid);
        game.addEntity(proj);
    }

}
