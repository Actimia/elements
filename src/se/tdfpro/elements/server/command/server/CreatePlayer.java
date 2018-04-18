package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

public class CreatePlayer extends ServerCommand {
    @Send
    public int controller;
    @Send
    public int eid;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;
//    @Send
//    public String username;

    public CreatePlayer() {}

    public CreatePlayer(Player player) {
        eid = player.getEid();
        controller = player.getController();
//        username = name;
        position = player.getPosition();
        velocity = player.getVelocity();
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreatePlayer(eid="+ eid + ", pid=" + controller + ")");
        Player player = new Player(position, velocity, controller);
        player.setEid(eid);

        game.addEntity(player);

    }
}
