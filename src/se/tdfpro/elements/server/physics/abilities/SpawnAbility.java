package se.tdfpro.elements.server.physics.abilities;

import se.tdfpro.elements.net.command.client.CastAbility;
import se.tdfpro.elements.server.GameServer;

@FunctionalInterface
public interface SpawnAbility {
    void execute(GameServer game, CastAbility cast);
}
