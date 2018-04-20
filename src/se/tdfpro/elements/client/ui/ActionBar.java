package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.Abilities;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;


public class ActionBar extends InterfaceComponent {
    private final Player caster;
    private Vec2 offset = new Vec2(64, 0);


    public ActionBar(Vec2 position, Player caster) {
        super(position);
        this.caster = caster;
    }

    public ActionBar addAbility(Abilities ability, Keybind key) {
        int index = children.size();
        children.add(ability.createIcon(caster, offset.scale(index), key));
        return this;
    }

    @Override
    public void draw(GameClient game, Graphics g) {
    }
}
