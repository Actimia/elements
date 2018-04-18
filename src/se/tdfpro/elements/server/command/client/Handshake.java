package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    public Handshake() {}

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (pid " + pid + ")");

        game.send(pid, new HandshakeReply(pid));

        // send current state
        game.getEntities().forEach(e -> game.send(pid, e.makeCreateCommand()));

        game.spawnEntity(new Player(new Vec2(100, 195), new Vec2(-15, 0), pid));

    }
}
