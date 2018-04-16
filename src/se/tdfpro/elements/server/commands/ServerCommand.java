package se.tdfpro.elements.server.commands;

import se.tdfpro.elements.server.engine.Game;

public interface ServerCommand {

    byte[] encode();

    public void execute(Game game);
}
