package se.tdfpro.elements.server.physics.abilities;

import org.newdawn.slick.Input;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

import java.util.function.Predicate;

public class Blink extends Ability {

    private static final SpawnAbility onSpawn = ((game, cast) -> {
        var source = (Player) game.getEntity(cast.sourceEid);

        var target = cast.target;
        var direction = target.sub(source.getPosition());
        var maxrange = 300f;
        if(direction.length2() > maxrange*maxrange) {
            target = source.getPosition().add(direction.norm().scale(maxrange));
        }

        source.setPosition(target);
    });

    static {
        Ability.registerAbility(Blink.class, onSpawn);
    }

    private static final float MAX_COOLDOWN = 3f;
    public Blink(Player player, Vec2 position, Predicate<Input> keybind) {
        super(player, position, keybind, MAX_COOLDOWN);
    }
}
