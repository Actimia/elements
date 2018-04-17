package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;

public class PlayerDisconnect extends ServerCommand {

    public PlayerDisconnect(){};

    @Send
    public int playerid;

    @Override
    public void execute(GameClient game) {

    }
}
