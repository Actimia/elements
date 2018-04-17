package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.CreateEntity;
import se.tdfpro.elements.server.command.server.CreatePlayer;
import se.tdfpro.elements.server.command.server.TakeControl;
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

        game.send(pid, new HandshakeReply(pid));

        // send current state
        var ents = game.getEntities().values();
        ents.forEach(e -> {
            var cmd = new CreateEntity(e);
            game.send(pid, cmd);
        });

        var ent = new PhysicsEntity(new Vec2(100, 195), new Vec2(-15,0), 30f);
        game.spawnEntity(ent);
        game.broadcast(new CreateEntity(ent));

        var ent2 = new PhysicsEntity(new Vec2(-200, 205), new Vec2(40,1), 30f);
        game.spawnEntity(ent2);
        game.broadcast(new CreateEntity(ent));

        var ent3 = new PhysicsEntity(new Vec2(0, 0), new Vec2(0,0), 30f);
        game.spawnEntity(ent3);
        game.broadcast(new CreatePlayer(ent, pid, username));

    }
}
