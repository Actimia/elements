package se.tdfpro.elements.net.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Ray;

public class CreateRay extends ServerCommand {
    @Send
    public int eid;

    @Send
    public Vec2 position;

    @Send
    public Vec2 direction;

    public CreateRay() {
    }

    public CreateRay(Ray ray) {
        this.eid = ray.getEid();
        this.position = ray.getPosition();
        this.direction = ray.getDirection();
    }

    @Override
    public void execute(GameClient game) {
        Ray ray = new Ray(position, direction);
        ray.setEid(eid);
        game.addEntity(ray);
    }
}
