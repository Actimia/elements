package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.client.Abilities;
import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.Player;

public class AbilityIcon extends InterfaceComponent {

    private static final float size = 56f;

    private final Abilities type;
    private final Player source;
    private float cooldown;
    private final Keybind keybind;
    private final Image image;

    public AbilityIcon(Abilities type, Player player, Vec2 position, Keybind keybind) {
        super(position);
        this.type = type;
        this.source = player;
        this.keybind = keybind;

        // will get the dynamic class, not just AbilityIcon
        var imageRef = "ability-" + type.toString().toLowerCase();
        this.image = GameClient.textures.get(imageRef);

        var keybindLabel = new Label(new Vec2(size/2, size + 5), keybind::getMnemonic);
        keybindLabel.setCenteredHorizontal(true);
        children.add(keybindLabel);

        var omniCC = new Label(new Vec2(size/2, size/2), () -> cooldown > 0 ? String.format("%.1f", cooldown) : "");
        omniCC.setColor(Color.red);
        omniCC.setCenteredHorizontal(true);
        omniCC.setCenteredVertical(true);
        children.add(omniCC);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        g.drawImage(image, 0, 0);

        if (cooldown > 0) {
            var cd = 1 - cooldown / type.getMaxCooldown();
            g.setColor(new Color(0f, 0f, 0f, 0.5f));
            g.fillRect(0, cd * size, size, (1f - cd) * size);
        }

        g.setColor(Color.white);
        g.drawRect(0, 0, size, size);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {
        super.updateClient(gc, game, delta);
        this.cooldown -= delta;
        var input = gc.getInput();
        if (keybind.test(input)) {
            if (cooldown <= 0) {
                cooldown = type.getMaxCooldown();

                var cmd = new CastAbility();
                cmd.spellRef = type.toString();
                cmd.sourceEid = source.getEid();

                cmd.target = game.camera.unproject(new Vec2(input.getMouseX(), input.getMouseY()));

                game.send(cmd);
            }
        }
    }
}
