package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import se.tdfpro.elements.client.Abilities;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.util.Box;
import se.tdfpro.elements.util.Vec2;
import se.tdfpro.elements.entity.physics.PlayerEntity;

public class PlayerInterface extends InterfaceComponent {
    private float ticks = 0;
    private float maxticks = 5f;

    public PlayerInterface(PlayerEntity player) {
        super(new Vec2(676, 800));
        var hpbar = new ProgressBar(new Box(0, 0, 248, 20), () -> ticks / maxticks);
        addChild(hpbar);

        var hplabel = new Label(new Vec2(124, 10), () -> String.format("%.1f/%.1f", ticks, maxticks));
        hplabel.setCenteredHorizontal(true);
        hplabel.setCenteredVertical(true);
        hplabel.setColor(Color.black);
        addChild(hplabel);

        var actionbar = new ActionBar(new Vec2(0, 28), player);
        actionbar.addAbility(Abilities.FIREBALL, Keybind.mouse(Input.MOUSE_LEFT_BUTTON));
        actionbar.addAbility(Abilities.BLINK, Keybind.mouse(Input.MOUSE_RIGHT_BUTTON));
        actionbar.addAbility(Abilities.KICK, Keybind.key(Input.KEY_1));
        actionbar.addAbility(Abilities.SHIELDWALL, Keybind.key(Input.KEY_2));
        addChild(actionbar);
    }

    @Override
    public void update(GameClient game, float delta) {
        super.update(game, delta);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
    }
}
