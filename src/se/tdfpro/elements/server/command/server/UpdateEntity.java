package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.DrawableEntity;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.engine.PhysicsEntity;
import se.tdfpro.elements.server.engine.Vec2;

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
        eid = ent.id;
        position = ent.position;
        velocity = ent.velocity;
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("UpdateEntity");
        var ent = (DrawableEntity) game.getEntity(eid);
        if(ent == null) return;
        ent.position = position.toVector2f();
        ent.velocity = velocity.toVector2f();

        System.out.println(position);
        System.out.println(ent.position);
        System.out.println(velocity);
        System.out.println(ent.velocity);
    }
}
