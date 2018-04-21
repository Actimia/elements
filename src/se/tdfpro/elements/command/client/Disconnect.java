package se.tdfpro.elements.command.client;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.server.PlayerDisconnect;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.entity.PlayerEntity;

public class Disconnect extends ClientCommand {
    @Send
    public int pid;

    @Override
    public void execute(GameServer game) {
//        game.getEntities().stream()
//            .filter(ent -> ent instanceof PlayerEntity)
//            .map(ent -> (PlayerEntity) ent)
//            .filter(p -> p.getController() == pid)
//            .findFirst()
//            .ifPresent(player -> {
//                var dc = new PlayerDisconnect();
//                dc.pid = pid;
//                game.broadcast(dc);
//                game.removeEntity(player.getId());
//            });
    }
}
