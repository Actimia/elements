package se.tdfpro.elements.server.command.server;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Player;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.engine.Vec2;

public class HandshakeReply extends ServerCommand {
    public HandshakeReply() {}

    @Send
    public int playerid;

    @Override
    public void execute(GameClient game) {
        Log.info("handshakereply");
        var player = new Player(game.camera);
        player.position = new Vector2f(200, 200);
        game.addEntity(player);
    }
}
