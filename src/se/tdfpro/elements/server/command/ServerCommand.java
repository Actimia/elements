package se.tdfpro.elements.server.command;

import se.tdfpro.elements.client.MainState;

public abstract class ServerCommand implements Command { // from server

    public byte[] encode() {
        Encoder encoder = new Encoder();
        encoder.encode(this);

        return encoder.getBytes();
    }

    public abstract void execute(MainState game);
}
