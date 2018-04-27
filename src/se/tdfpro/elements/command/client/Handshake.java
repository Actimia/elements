package se.tdfpro.elements.command.client;

import org.newdawn.slick.Color;
import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.server.CreateEntity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PlayerEntity;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    @Send
    public Color color;

    public Handshake() {
    }

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (pid " + pid + ")");

        game.send(pid, new HandshakeReply(pid));

        var world = game.getRoot();

        // send current state
        world.tree()
            .filter(e -> e.getId() >= 0)
            .forEach(e -> game.send(pid, new CreateEntity(e)));

        var player = new PlayerEntity(new Vec2(100, 195), new Vec2(-15, 0), pid, username, color);
        player.init(game);
        world.addChild(player);
    }
}
