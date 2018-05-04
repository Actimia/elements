package se.tdfpro.elements.command.client;

import org.newdawn.slick.Color;
import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.server.CreateEntity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Vec2;
import se.tdfpro.elements.entity.physics.Player;

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

        // send current state
        game.getEntity(0).tree()
            .skip(1) // the world itself already exists on client
            .forEach(ent -> game.send(pid, new CreateEntity(ent)));

        var player = new Player(new Vec2(100, 195), new Vec2(-15, 0), pid, username, color);
        game.createEntity(0, player);
    }
}
