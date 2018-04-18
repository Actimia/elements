package se.tdfpro.elements.server.physics.entity;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.command.ServerCommand;

public interface ServerEntity {

    void updateServer(GameServer game, float delta) ;

    ServerCommand makeCreateCommand();
}
