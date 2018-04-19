package se.tdfpro.elements.command.client;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;

public class HandshakeReply extends ServerCommand {

    @Send
    public int pid;

    public HandshakeReply() {
    }

    public HandshakeReply(int pid) {
        this.pid = pid;
    }

    @Override
    public void execute(GameClient game) {
        game.setPid(pid);
    }
}
