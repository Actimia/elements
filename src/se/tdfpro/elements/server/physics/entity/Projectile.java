package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.DecodeConstructor;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Projectile extends Circle {
    private int bounces = 5;
    private final int sourceEid;

    @DecodeConstructor
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
    public void draw(GameClient game, Graphics g) {
        var source = (Player) game.getEntity(sourceEid);
        var color = source.getColor();

        var gradient = GameClient.textures.get("gradient").getScaledCopy(0.5f);
        g.drawImage(gradient, -gradient.getWidth() / 2, -gradient.getHeight() / 2, color);
        g.fillOval(-radius, -radius, 2 * radius, 2 * radius);
    }

    @Override
    public void onCollide(GameServer game, PhysicsEntity other) {
        if (--bounces == 0) {
            game.deleteEntity(getEid());
        }
    }
}
