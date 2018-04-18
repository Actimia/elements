package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;

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
