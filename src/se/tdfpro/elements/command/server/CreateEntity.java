package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;

public class CreateEntity extends ServerCommand {
    @Send
    public PhysicsEntity entity;

    @Override
    public void execute(GameClient game) {
        game.addEntity(entity);
    }
}
