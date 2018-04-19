package se.tdfpro.elements.server.physics.abilities;

import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;
import se.tdfpro.elements.server.physics.entity.Projectile;

public class Fireball extends Ability {
    private static final SpawnAbility onSpawn = ((game, cast) -> {
        var source = game.getEntity(cast.sourceEid);
        var direction = cast.target.sub(source.getPosition()).norm();
        var ball = new Projectile(source.getPosition().add(direction.scale(45f)), direction.scale(400), source);
        game.spawnEntity(ball);
    });

    static {
        Ability.registerAbility(Fireball.class, onSpawn);
    }

    private static final float MAX_COOLDOWN = 1.5f;

    public Fireball(Player player, Vec2 position, Keybind keybind) {
        super(player, position, keybind, MAX_COOLDOWN);
    }
}
