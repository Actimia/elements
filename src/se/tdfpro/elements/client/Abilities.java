package se.tdfpro.elements.client;

import se.tdfpro.elements.client.ui.AbilityIcon;
import se.tdfpro.elements.client.ui.Cooldown;
import se.tdfpro.elements.client.ui.MultiCooldown;
import se.tdfpro.elements.client.ui.SingleCooldown;
import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.entity.physics.PhysicsEntity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Vec2;
import se.tdfpro.elements.entity.physics.PlayerEntity;
import se.tdfpro.elements.entity.physics.Projectile;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Abilities {
    FIREBALL((game, cast) -> {
        var source = (PhysicsEntity) game.getEntity(cast.sourceEid);
        var direction = cast.target.sub(source.getPosition()).norm();
        var fireball = new Projectile(source.getPosition().add(direction.scale(45f)), direction.scale(400), source.getId
            ());
        game.createEntity(0, fireball);
    }, 1.5f),
    BLINK((game, cast) -> {
        var source = (PhysicsEntity) game.getEntity(cast.sourceEid);

        var target = cast.target;
        var direction = target.sub(source.getPosition());
        var maxRange = 300f;
        if (direction.length2() > maxRange * maxRange) {
            target = source.getPosition().add(direction.norm().scale(maxRange));
        }

        source.setPosition(target);
    }, 5f, Cooldowns.GCD),
    KICK((game, cast) -> {

    }, 10f, Cooldowns.GCD, Cooldowns.kickandsw),
    SHIELDWALL((game, cast) -> {
        var source = game.getEntity(cast.sourceEid);
//        source.addStatusEffect();
    }, 30f, Cooldowns.GCD, Cooldowns.kickandsw);

    private final SpawnAbility onSpawn;
    private final float maxCooldown;
    private final Cooldown[] sharedCooldowns;

    Abilities(SpawnAbility onSpawn, float ownCooldown, Cooldown... sharedCooldowns) {
        this.onSpawn = onSpawn;
        this.maxCooldown = ownCooldown;
        this.sharedCooldowns = sharedCooldowns;
    }

    public void onSpawn(GameServer game, CastAbility cast) {
        onSpawn.execute(game, cast);
    }

    public AbilityIcon createIcon(PlayerEntity source, Vec2 position, Keybind keybind) {

        return new AbilityIcon(this, source, position, keybind, createCooldown());
    }

    private Cooldown createCooldown() {
        var cooldowns = Stream.concat(
            Arrays.stream(sharedCooldowns),
            Stream.of(new SingleCooldown(maxCooldown))
        ).filter(cd -> cd.getMax() > 0).collect(Collectors.toList());

        switch (cooldowns.size()) {
            case 0:
                throw new RuntimeException("Need at least one cooldown for " + this);
            case 1:
                return cooldowns.get(0);
            default:
                return new MultiCooldown(cooldowns);
        }
    }

    public float getMaxCooldown() {
        return maxCooldown;
    }

    private static class Cooldowns {
        private static final Cooldown GCD = new SingleCooldown(750);
        private static final Cooldown kickandsw = new SingleCooldown(5f);
    }
}


