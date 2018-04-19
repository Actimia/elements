package se.tdfpro.elements.net.command.client;

import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.abilities.Ability;
import se.tdfpro.elements.server.physics.entity.Projectile;

public class CastAbility extends ClientCommand {

    @Send
    public int sourceEid;

    @Send
    public Vec2 target;

    @Send
    public String spellRef;

    public CastAbility() {
    }

    @Override
    public void execute(GameServer game) {
        Ability.getAbility(spellRef).execute(game, this);
    }
}
