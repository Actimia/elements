package se.tdfpro.elements.server.physics.entity;

import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.server.GameServer;

public interface ServerEntity {

    void updateServer(GameServer game, float delta);

    void encodeConstructorParams(Encoder encoder);
}
