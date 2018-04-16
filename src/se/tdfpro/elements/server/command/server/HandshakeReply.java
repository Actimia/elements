package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.MainState;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.engine.Vec2;

public class HandshakeReply extends ServerCommand {
    public HandshakeReply() {}
    @Send
    public int playerid;

    @Send
    public Vec2 startPosition;

    @Override
    public void execute(MainState game) {

    }
}
