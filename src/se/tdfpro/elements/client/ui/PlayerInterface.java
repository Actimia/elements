package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import se.tdfpro.elements.client.Abilities;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.server.physics.Box;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

public class PlayerInterface extends InterfaceComponent {


    public PlayerInterface(Player player) {
        super(new Vec2(800, 800));
        var hpbar = new ProgressBar(new Box(0, 0, 200, 20), () -> 0.7f);
        children.add(hpbar);


        var actionbar = new ActionBar(new Vec2(0, 28), player);
        actionbar.addAbility(Abilities.FIREBALL, Keybind.mouse(Input.MOUSE_LEFT_BUTTON));
        actionbar.addAbility(Abilities.BLINK, Keybind.key(Input.KEY_LSHIFT));
        children.add(actionbar);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
    }

}
