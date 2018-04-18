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
        g.setLineWidth(3);
        g.setColor(Color.white);
        var dst = direction.scale(2000f);
        var src = dst.invert();
        g.drawLine(src.x, src.y, dst.x, dst.y);
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

    public boolean isRHS(Vec2 point) {
        return point.sub(position).dot(direction.perpendicular()) > 0;
    }

    public float closestDistance(Vec2 point) {
        return point.sub(position).dot(direction.perpendicular());
    }

    @Override
    public ServerCommand makeCreateCommand() {
        return new CreateRay(this);
    }
}
