package se.tdfpro.elements.server.physics.entity;

import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.server.GameServer;

public interface ServerEntity {

    void updateServer(GameServer game, float delta);

    ServerCommand makeCreateCommand();
}
