package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.command.ServerCommand;
import se.tdfpro.elements.command.server.CreateProjectile;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Projectile extends Circle {
    private int bounces = 5;
    private final int sourceEid;

    public Projectile(Vec2 position, Vec2 velocity, int sourceEid) {
        super(position, velocity, 1f, Materials.PROJECTILE, 10f);
        this.sourceEid = sourceEid;
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {
        encoder.encode(position);
        encoder.encode(velocity);
        encoder.encode(sourceEid);
    }

    @Override
    public ServerCommand makeCreateCommand() {
        var res = new CreateProjectile();
        res.position = position;
        res.velocity = velocity;
        res.eid = getEid();
        res.sourceId = sourceEid;
        return res;
    }

    @Override
    public void draw(Graphics g) {

        var img = GameClient.textures.get("gradient");
        g.drawImage(img, -50, -50, Color.red);
        g.fillOval(-radius, -radius, 2 * radius, 2 * radius);
    }

    @Override
    public void onCollide(GameServer game, PhysicsEntity other) {
        if (--bounces == 0) {
            game.deleteEntity(getEid());
        }
    }
}
