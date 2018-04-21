package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.entity.Entity;

public class DestroyEntity extends ServerCommand {
    @Send
    public int eid;

    public DestroyEntity() {
    }

    public DestroyEntity(Entity ent) {
        this.eid = ent.getId();
    }

    @Override
    public void execute(GameClient game) {
        game.deleteEntity(eid);
    }
}
