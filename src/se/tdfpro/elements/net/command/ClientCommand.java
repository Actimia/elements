package se.tdfpro.elements.net.command;

import se.tdfpro.elements.server.GameServer;

public abstract class ClientCommand implements Command { // from client

    public byte[] encode() {
        return Encoder.encodeCommand(this);
    }

    public int pid = -1;

    public abstract void execute(GameServer game);
}


