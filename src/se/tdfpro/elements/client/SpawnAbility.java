package se.tdfpro.elements.client;

import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.server.GameServer;

@FunctionalInterface
public interface SpawnAbility {
    void execute(GameServer game, CastAbility cast);
}
