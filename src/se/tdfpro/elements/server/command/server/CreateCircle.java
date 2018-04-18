package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.entity.Circle;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

public class CreateCircle extends ServerCommand {
    @Send
    public int eid;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;

    public CreateCircle() {}

    public CreateCircle(PhysicsEntity ent) {
        eid = ent.getEid();
        position = ent.getPosition();
        velocity = ent.getVelocity();
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreateCircle("+ eid + ")");
        var player = new Circle(position, velocity, 0.5f, Materials.PLAYER, 30f);
        player.setEid(eid);
        game.addEntity(player);
    }
}
