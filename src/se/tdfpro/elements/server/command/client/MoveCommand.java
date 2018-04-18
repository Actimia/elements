package se.tdfpro.elements.server.command.client;

import org.newdawn.slick.geom.Vector2f;
import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.shapes.Player;

public class MoveCommand extends ClientCommand {

    public MoveCommand() {}

    @Send
    public int eid;
    @Send
    public Vec2 direction;

    public MoveCommand(int id, Vector2f movement) {
        eid = id;
        direction = new Vec2(movement);
    }


    @Override
    public void execute(GameServer game) {
        var player = (Player) game.getEntities().get(eid);
        player.impulse = direction.scale(100f);
    }
}
