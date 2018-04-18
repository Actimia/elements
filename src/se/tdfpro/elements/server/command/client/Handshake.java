package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.CreateEntity;
import se.tdfpro.elements.server.command.server.CreatePlayer;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.PhysicsEntity;
import se.tdfpro.elements.server.physics.Vec2;

public class Handshake extends ClientCommand {
    public Handshake(){};
    @Send
    public String username;

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (pid " + pid + ")");

        game.send(pid, new HandshakeReply(pid));

        // send current state
        var ents = game.getEntities().values();
        ents.forEach(e -> {
            var cmd = new CreateEntity(e);
            game.send(pid, cmd);
        });

        var ent = new PhysicsEntity(new Vec2(100, 195), new Vec2(-15,0), 30f);
        game.spawnEntity(ent);
        System.out.println(ent.id);
        game.broadcast(new CreateEntity(ent));

        var ent2 = new PhysicsEntity(new Vec2(-200, 205), new Vec2(40,1), 30f);
        game.spawnEntity(ent2);
        System.out.println(ent2.id);
        game.broadcast(new CreateEntity(ent2));

        var ent3 = new PhysicsEntity(new Vec2(0, 0), new Vec2(0,0), 30f);
        game.spawnEntity(ent3);
        System.out.println(ent3.id);
        game.broadcast(new CreatePlayer(ent3, pid, username));

    }
}
