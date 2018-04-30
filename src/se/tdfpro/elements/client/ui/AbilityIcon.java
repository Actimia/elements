package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.Abilities;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.util.Vec2;
import se.tdfpro.elements.entity.physics.PlayerEntity;

public class AbilityIcon extends Sprite {

    private static final float size = 56f;

    private final Abilities type;
    private final PlayerEntity source;
    private final Cooldown cooldown;
    private final Keybind keybind;

    public AbilityIcon(Abilities type, PlayerEntity player, Vec2 position, Keybind keybind, Cooldown cooldown) {
        super(position, "ability-" + type.toString().toLowerCase());
        this.type = type;
        this.source = player;
        this.keybind = keybind;
        this.cooldown = cooldown;

        var keybindLabel = new Label(new Vec2(size / 2, size + 5), this.keybind::getMnemonic);
        keybindLabel.setCenteredHorizontal(true);
        addChild(keybindLabel);

        var omniCC = new Label(new Vec2(size / 2, size / 2), this.cooldown::getRemainingText);
        omniCC.setColor(Color.red);
        omniCC.setCenteredHorizontal(true);
        omniCC.setCenteredVertical(true);
        addChild(omniCC);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        super.draw(game, g);

        var cd = cooldown.getRemainingQuotient();
        if (cd > 0) {
            g.setColor(new Color(0f, 0f, 0f, 0.7f));
            g.fillRect(0, (1 - cd) * size, size, cd * size);
        }

        g.setColor(Color.white);
        g.drawRect(0, 0, size, size);
    }

    @Override
    public void update(GameClient game, float delta) {
        var input = game.getInput();
        if (keybind.test(input)) {
            if (cooldown.ready()) {
                cooldown.start();

                var cmd = new CastAbility();
                cmd.spellRef = type.toString();
                cmd.sourceEid = source.getId();
                cmd.target = game.camera.unproject(new Vec2(input.getMouseX(), input.getMouseY()));

                game.send(cmd);
            }
        }
    }
}
