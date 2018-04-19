package se.tdfpro.elements.command;

import se.tdfpro.elements.client.GameClient;

public abstract class ServerCommand implements Command { // from server

    public byte[] encode() {
        return Encoder.encodeCommand(this);
    }

    public abstract void execute(GameClient game);
}
