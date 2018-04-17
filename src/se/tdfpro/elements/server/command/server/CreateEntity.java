package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.Entity;
import se.tdfpro.elements.client.engine.entity.Player;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.engine.Vec2;

public class CreateEntity extends ServerCommand {
    @Send
    public int id;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;

    @Override
    public void execute(GameClient game) {
        System.out.println("CreateEntity");
        var player = new Player(id, "");
        player.position = position.toVector2f();
        player.velocity = position.toVector2f();

        System.out.println(position);
        System.out.println(player.position);
        System.out.println(velocity);
        System.out.println(player.velocity);
        game.addEntity(player);
    }
}
