package se.tdfpro.elements.command.client;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PlayerEntity;

public class PlayerMove extends ClientCommand {

    public PlayerMove() {
    }

    @Send
    public int eid;
    @Send
    public Vec2 direction;

    public PlayerMove(int id, Vec2 movement) {
        eid = id;
        direction = movement;
    }

    @Override
    public void execute(GameServer game) {
        var player = (PlayerEntity) game.getEntity(eid);
        player.setImpulse(direction.scale(50f));
    }
}
