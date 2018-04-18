package se.tdfpro.elements.server.command.client;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.Send;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Circle;
import se.tdfpro.elements.server.physics.entity.Projectile;

public class CastSpell extends ClientCommand {

    @Send
    public int sourceEid;

    @Send
    public Vec2 direction;

    public CastSpell() {}

    public CastSpell(Vec2 dir, int sourceEid) {
        this.direction = dir;
        this.sourceEid = sourceEid;
    }

    @Override
    public void execute(GameServer game) {
        var source = game.getEntity(sourceEid);
        var ball = new Projectile(source.getPosition().add(direction.scale(35f)), direction.scale(400));
        game.spawnEntity(ball);
    }
}
