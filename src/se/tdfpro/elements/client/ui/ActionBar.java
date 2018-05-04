package se.tdfpro.elements.client.ui;

import se.tdfpro.elements.client.ability.Abilities;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.entity.physics.Player;
import se.tdfpro.elements.util.Vec2;

public class ActionBar extends InterfaceComponent {
    private final Player caster;
    private Vec2 offset = new Vec2(64, 0);
    private int index = 0;

    public ActionBar(Vec2 position, Player caster) {
        super(position);
        this.caster = caster;
    }

    public ActionBar addAbility(Abilities ability, Keybind key) {
        addChild(ability.createIcon(caster, offset.scale(index++), key));
        return this;
    }
}
