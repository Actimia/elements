package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;

public class PlayerJoined extends ServerCommand {

    public PlayerJoined(){};

    @Send
    public int playerid;

    @Send
    public String username;

    @Override
    public void execute(GameClient game) {

    }
}
