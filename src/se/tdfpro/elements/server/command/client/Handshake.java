package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.CreateEntity;
import se.tdfpro.elements.server.engine.GameServer;
import se.tdfpro.elements.server.engine.PhysicsEntity;
import se.tdfpro.elements.server.engine.Vec2;

public class Handshake extends ClientCommand {
    public Handshake(){};
    @Send
    public String username;

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (id " + pid + ")");

        var ent = new PhysicsEntity(new Vec2(100, 195), new Vec2(-15,0), 30f);
        game.spawnEntity(ent);

        var reply = new CreateEntity();
        reply.position = ent.position;
        reply.velocity = ent.velocity;
        reply.id = ent.id;
        game.broadcast(reply);

        var ent2 = new PhysicsEntity(new Vec2(-200, 205), new Vec2(40,1), 30f);
        game.spawnEntity(ent2);

        var reply2 = new CreateEntity();
        reply2.position = ent2.position;
        reply2.velocity = ent2.velocity;
        reply2.id = ent2.id;
        game.broadcast(reply2);
    }
}
