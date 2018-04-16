package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.engine.Game;

public class Handshake extends ClientCommand {
    @Send
    public String username;

    @Override
    public void execute(Game game) {

    }
}
