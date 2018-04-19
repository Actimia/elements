package se.tdfpro.elements.net.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.net.command.ServerCommand;

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
