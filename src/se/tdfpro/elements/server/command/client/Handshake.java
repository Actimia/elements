package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.HandshakeReply;
import se.tdfpro.elements.server.engine.GameServer;
import se.tdfpro.elements.server.engine.PhysicsEntity;
import se.tdfpro.elements.server.engine.Vec2;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (id " + pid + ")");

        var reply = new HandshakeReply();
        reply.playerid = pid;
        game.send(pid, reply);

        game.spawnEntity(new PhysicsEntity(new Vec2(200, 200), new Vec2(0,0), 40f));
    }
}
