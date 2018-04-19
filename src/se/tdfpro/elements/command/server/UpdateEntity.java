package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;

public class UpdateEntity extends ServerCommand {

    @Send
    public int eid;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;

    public UpdateEntity() {}

    public UpdateEntity(PhysicsEntity ent) {
        eid = ent.getEid();
        position = ent.getPosition();
        velocity = ent.getVelocity();
    }

    @Override
    public void execute(GameClient game) {
        var ent = game.getEntity(eid);
        if (ent == null) return;
        ent.setPosition(position);
        ent.setVelocity(velocity);
    }
}
