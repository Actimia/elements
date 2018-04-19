package se.tdfpro.elements.client.abilities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.Keybind;
import se.tdfpro.elements.command.client.CastAbility;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.ClientEntity;
import se.tdfpro.elements.server.physics.entity.Player;

public class Ability implements ClientEntity {

    private static final float size = 56f;

    private final Abilities type;
    private final Player source;
    private final Vec2 position;
    private float cooldown;
    private final Keybind keybind;
    private final Image image;

    // package access is intentional
    Ability(Abilities type, Player player, Vec2 position, Keybind keybind) {
        this.type = type;
        this.source = player;
        this.position = position;
        this.keybind = keybind;

        // will get the dynamic class, not just Ability
        var imageRef = "ability-" + type.toString().toLowerCase();
        this.image = GameClient.textures.get(imageRef);
    }

    @Override
    public void render(GameContainer gc, GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        draw(game, g);
        g.popTransform();
    }

    @Override
    public final void init(GameClient game) {
        // not called for abilities
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        g.drawImage(image, 0, 0);

        var font = g.getFont();
        if (cooldown > 0) {
            var cd = 1 - cooldown / type.getMaxCooldown();
            g.setColor(new Color(0f, 0f, 0f, 0.45f));
            g.fillRect(0, cd * size, size, (1f - cd) * size);

            g.setColor(Color.red);
            var text = String.format("%.1f", cooldown);
            var x = (size - font.getWidth(text)) / 2;
            var y = (size - font.getHeight(text)) / 2;
            g.drawString(text, x, y);
        }

        g.setColor(Color.white);
        g.drawRect(0, 0, size, size);
        var text = keybind.getMnemonic();
        var x = (size - g.getFont().getWidth(text)) / 2;
        g.drawString(text, x, size);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {
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
