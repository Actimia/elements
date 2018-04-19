package se.tdfpro.elements.net.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Projectile;

public class CreateProjectile extends ServerCommand {
    @Send
    public int eid;

    @Send
    public int sourceId;

    @Send
    public Vec2 position;

    @Send
    public Vec2 velocity;

    public CreateProjectile() {
    }

    @Override
    public void execute(GameClient game) {
        var projectile = new Projectile(position, velocity, game.getEntity(sourceId));
        projectile.setEid(eid);
        game.addEntity(projectile);
    }

}
