package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.engine.Game;
import se.tdfpro.elements.server.engine.Vec2;

public class MoveCommand extends ClientCommand {

    public MoveCommand() {}

    @Send
    public Vec2 velo;

    @Send
    public String something;

    @Override
    public void execute(Game game) {
        System.out.println(player + " " + velo + " " + something);
    }
}
