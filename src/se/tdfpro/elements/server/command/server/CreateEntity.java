package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.Entity;
import se.tdfpro.elements.client.engine.entity.Player;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.engine.PhysicsEntity;
import se.tdfpro.elements.server.engine.Vec2;

public class CreateEntity extends ServerCommand {
    @Send
    public int id;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;

    public CreateEntity() {}

    public CreateEntity(PhysicsEntity ent) {
        id = ent.id;
        position = ent.position;
        velocity = ent.velocity;
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreateEntity");
        var player = new Player(id, "" +id);
        player.position = position.toVector2f();
        player.velocity = position.toVector2f();
        game.addEntity(player);
    }
}
