package se.tdfpro.elements.net.command.server;

import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.Send;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Circle;

public class CreateCircle extends ServerCommand {
    @Send
    public int eid;
    @Send
    public Vec2 position;
    @Send
    public Vec2 velocity;
    @Send
    public float invMass;
    @Send
    public int material;
    @Send
    public float radius;

    public CreateCircle() {
    }

    public CreateCircle(Circle ent) {
        eid = ent.getEid();
        position = ent.getPosition();
        velocity = ent.getVelocity();
        invMass = ent.getInvMass();
        material = ((Materials) ent.getMaterial()).ordinal();
        radius = ent.radius;
    }

    @Override
    public void execute(GameClient game) {
        var player = new Circle(position, velocity, invMass, Materials.values()[material], radius);
        player.setEid(eid);
        game.addEntity(player);
    }
}
