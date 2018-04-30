package se.tdfpro.elements.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Send;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.entity.Entity;

public class CreateEntity extends ServerCommand {
    @Send
    public int parentid;
    @Send
    public Entity entity;

    public CreateEntity() {}

    public CreateEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void execute(GameClient game) {
        game.addEntity(entity.init(game));
    }
}
