package se.tdfpro.elements.net.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.net.command.ServerCommand;

public class PlayerDisconnect extends ServerCommand {

    public PlayerDisconnect() {}

    @Send
    public int pid;

    @Override
    public void execute(GameClient game) {

    }
}
