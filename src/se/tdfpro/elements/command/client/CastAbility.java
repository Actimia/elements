package se.tdfpro.elements.command.client;

import se.tdfpro.elements.client.abilities.Abilities;
import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.client.abilities.Ability;

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
        Abilities.valueOf(spellRef).onSpawn(game, this);
    }
}
