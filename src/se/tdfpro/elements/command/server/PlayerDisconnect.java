package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;

public class PlayerDisconnect extends ServerCommand {

    @Send
    public int pid;

    public PlayerDisconnect() {}

    @Override
    public void execute(GameClient game) {

    }
}
