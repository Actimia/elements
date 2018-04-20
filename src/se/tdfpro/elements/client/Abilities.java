package se.tdfpro.elements.client;

import se.tdfpro.elements.client.ui.AbilityIcon;
import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;
import se.tdfpro.elements.server.physics.entity.Projectile;

public enum Abilities {

    FIREBALL(1.5f, (game, cast) -> {
        var source = game.getEntity(cast.sourceEid);
        var direction = cast.target.sub(source.getPosition()).norm();
        var ball = new Projectile(source.getPosition().add(direction.scale(45f)), direction.scale(400), source.getEid());
        game.spawnEntity(ball);
    }),
    BLINK(3f, (game, cast) -> {
        var source = game.getEntity(cast.sourceEid);

        var target = cast.target;
        var direction = target.sub(source.getPosition());
        var maxRange = 300f;
        if (direction.length2() > maxRange * maxRange) {
            target = source.getPosition().add(direction.norm().scale(maxRange));
        }

        source.setPosition(target);
    });

    private final SpawnAbility onSpawn;
    private final float maxCooldown;

    Abilities(float maxCooldown, SpawnAbility onSpawn) {
        this.onSpawn = onSpawn;
        this.maxCooldown = maxCooldown;
    }

    public void onSpawn(GameServer game, CastAbility cast) {
        onSpawn.execute(game, cast);
    }

    public AbilityIcon createIcon(Player source, Vec2 position, Keybind keybind) {
        return new AbilityIcon(this, source, position, keybind);
    }

    public float getMaxCooldown() {
        return maxCooldown;
    }
}
