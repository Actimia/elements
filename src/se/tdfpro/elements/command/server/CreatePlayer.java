package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
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
    @Send
    public String username;

    public CreatePlayer() {
    }

    public CreatePlayer(Player player) {
        eid = player.getEid();
        controller = player.getController();
        username = player.getUsername();
        position = player.getPosition();
        velocity = player.getVelocity();
    }

    @Override
    public void execute(GameClient game) {
        Player player = new Player(position, velocity, controller, username);
        player.setEid(eid);

        game.addEntity(player);

        if (game.getPid() == controller) {
            game.createAbilities(player);
        }
    }
}
