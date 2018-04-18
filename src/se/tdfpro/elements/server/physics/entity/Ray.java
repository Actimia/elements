package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.CreateRay;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Ray extends PhysicsEntity {

    private final Vec2 direction;

    public Ray(Vec2 position, Vec2 dir) {
        super(position, Materials.WALL);
        this.direction = dir.norm();
    }

    @Override
    public void draw(Graphics g) {
        g.setLineWidth(6);
        g.setColor(Color.white);
        var target = direction.scale(2000f);
        var from = position.sub(target);
        var to = position.add(target);
        g.drawLine(from.x, from.y, to.x, to.y);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {

    }

    @Override
    public String toString() {
        return String.format("Ray [%s->%s]", position, direction);
    }

    public Vec2 getDirection() {
        return direction;
    }

    @Override
    public void updateServer(GameServer game, float delta) {

    }

    @Override
    public ServerCommand makeCreateCommand() {
        return new CreateRay(this);
    }
}
