package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;

public class DeleteEntity extends ServerCommand {
    @Send
    public int eid;

    public DeleteEntity() {
    }

    public DeleteEntity(int eid) {
        this.eid = eid;
    }

    @Override
    public void execute(GameClient game) {
        game.deleteEntity(eid);
    }
}
