package se.tdfpro.elements.server.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.entity.ControlledPlayer;
import se.tdfpro.elements.client.engine.entity.Player;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.command.ServerCommand;

import javax.naming.ldap.Control;

public class TakeControl extends ServerCommand {

    @Send
    public int eid;

    public TakeControl() {};

    public TakeControl(int eid) {
        this.eid = eid;
    }

    @Override
    public void execute(GameClient game) {
        var entities = game.getEntities();

        var dumb = (Player) entities.get(eid);
        ControlledPlayer player = new ControlledPlayer(eid, "");
        player.position = dumb.position;
        player.velocity = dumb.velocity;
        player.facing = dumb.facing;

        entities.put(eid, player);
    }
}
