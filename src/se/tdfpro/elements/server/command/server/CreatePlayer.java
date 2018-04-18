package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.ControlledPlayer;
import se.tdfpro.elements.client.engine.entity.Player;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

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

    public CreatePlayer() {}

    public CreatePlayer(PhysicsEntity ent, int controllerpid, String name) {
        eid = ent.eid;
        controller = controllerpid;
        username = name;
        position = ent.position;
        velocity = ent.velocity;
    }

    @Override
    public void execute(GameClient game) {
        System.out.println("CreatePlayer("+ eid + ")");
        Player player;
        if(game.getPid() != controller){
            player = new Player(eid, username);
        } else {
            player = new ControlledPlayer(eid, username);
        }

        player.position = position.toVector2f();
        player.velocity = position.toVector2f();
        game.addEntity(player);

    }
}
