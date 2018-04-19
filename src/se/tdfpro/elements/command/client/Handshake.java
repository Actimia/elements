package se.tdfpro.elements.command.client;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.server.CreateEntity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    public Handshake() {
    }

    public Handshake(String name) {
        this.username = name;
    }

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (pid " + pid + ")");

        game.send(pid, new HandshakeReply(pid));

        // broadcast current state
        game.getEntities().forEach(e -> game.send(pid, new CreateEntity(e)));

        game.spawnEntity(new Player(new Vec2(100, 195), new Vec2(-15, 0), pid, username));
    }
}
