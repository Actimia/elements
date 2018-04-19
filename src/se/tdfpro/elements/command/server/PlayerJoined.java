package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;

public class PlayerJoined extends ServerCommand {

    public PlayerJoined() {}
    
    @Send
    public int pid;

    @Send
    public String username;

    @Override
    public void execute(GameClient game) {

    }
}
