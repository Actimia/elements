package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.server.physics.entity.PhysicsEntity;
import se.tdfpro.elements.server.physics.entity.Player;

public class CreateEntity extends ServerCommand {
    @Send
    public PhysicsEntity entity;

    public CreateEntity() {}

    public CreateEntity(PhysicsEntity entity) {
        this.entity = entity;
    }

    @Override
    public void execute(GameClient game) {
        game.addEntity(entity);
        entity.init(game);
    }
}
