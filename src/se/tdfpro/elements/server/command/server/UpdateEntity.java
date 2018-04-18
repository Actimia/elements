package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.DrawableEntity;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

public class UpdateEntity extends ServerCommand {

    @Send
    public int eid;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;

    public UpdateEntity() {

    }

    public UpdateEntity(PhysicsEntity ent) {
        eid = ent.eid;
        position = ent.position;
        velocity = ent.velocity;
    }

    @Override
    public void execute(GameClient game) {
        var ent = (DrawableEntity) game.getEntity(eid);
        if(ent == null) return;
        ent.position = position.toVector2f();
        ent.velocity = velocity.toVector2f();
    }
}
