package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.CreateEntity;
import se.tdfpro.elements.server.engine.GameServer;
import se.tdfpro.elements.server.engine.PhysicsEntity;
import se.tdfpro.elements.server.engine.Vec2;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (id " + pid + ")");



        var ent = new PhysicsEntity(new Vec2(200, 200), new Vec2(1,2), 30f);
        game.spawnEntity(ent);

        var reply = new CreateEntity();
        reply.position = ent.position;
        reply.velocity = ent.velocity;
        reply.id = ent.id;
        game.broadcast(reply);
    }
}
