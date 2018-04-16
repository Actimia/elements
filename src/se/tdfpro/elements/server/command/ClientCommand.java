package se.tdfpro.elements.server.command;

import se.tdfpro.elements.server.engine.Game;

public abstract class ClientCommand implements Command { // from client

    public byte[] encode() {
        Encoder encoder = new Encoder();
        encoder.encode(this);

        return encoder.getBytes();
    }

    @Send
    public int player;

    public abstract void execute(Game game);
}


