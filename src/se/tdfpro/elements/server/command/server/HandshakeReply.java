package se.tdfpro.elements.server.command.server;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.ControlledPlayer;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;

public class HandshakeReply extends ServerCommand {
    public HandshakeReply() {}

    @Send
    public String name;

    @Send
    public int id;

    @Override
    public void execute(GameClient game) {

        var player = new ControlledPlayer(id, name, game.camera);
        player.position = new Vector2f(200, 200);
        game.addEntity(player);
    }
}
