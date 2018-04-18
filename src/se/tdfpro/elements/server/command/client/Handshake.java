package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.server.CreateEntity;
import se.tdfpro.elements.server.command.server.CreatePlayer;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.shapes.Circle;
import se.tdfpro.elements.server.physics.shapes.Player;

public class Handshake extends ClientCommand {
    public Handshake(){};
    @Send
    public String username;

    @Override
    public void execute(GameServer game) {
        System.out.println(username + " has connected (pid " + pid + ")");

        game.send(pid, new HandshakeReply(pid));

        // send current state
        var ents = game.getEntities();
        ents.forEach(e -> {
            var cmd = new CreateEntity(e);
            game.send(pid, cmd);
        });

        var ent = new Circle(new Vec2(100, 1), new Vec2(-15,0), .5f, Materials.PLAYER, 30f);
        game.spawnEntity(ent);
        game.broadcast(new CreateEntity(ent));

        var ent2 = new Circle(new Vec2(300, 195), new Vec2(-15,0), .5f, Materials.PLAYER, 30f);
        game.spawnEntity(ent2);
        game.broadcast(new CreateEntity(ent2));

        var ent3 = new Player(new Vec2(100, 195), new Vec2(-15,0));
        game.spawnEntity(ent3);
        game.broadcast(new CreatePlayer(ent3, pid, username));

    }
}
