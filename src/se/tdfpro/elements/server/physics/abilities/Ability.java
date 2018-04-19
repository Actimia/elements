package se.tdfpro.elements.server.physics.abilities;

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

import java.util.HashMap;
import java.util.Map;

public abstract class Ability implements ClientEntity {
    private static final Map<String, SpawnAbility> abilities = new HashMap<>();

    public static void registerAbility(Class<? extends Ability> cls, SpawnAbility effect) {
        abilities.put(cls.getSimpleName(), effect);
    }

    public static SpawnAbility getAbility(String ref) {
        return abilities.get(ref);
    }

    private static final float size = 56f;

    private final Player source;
    private final Vec2 position;
    private float cooldown;
    private final float maxCooldown;
    private final Keybind keybind;
    private final Image image;

    public Ability(Player player, Vec2 position, Keybind keybind, float maxCooldown) {
        this.source = player;
        this.position = position;
        this.maxCooldown = maxCooldown;
        this.keybind = keybind;

        // will get the dynamic class, not just Ability
        var imageRef = "ability-" + getClass().getSimpleName().toLowerCase();
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
            var cd = 1 - cooldown / maxCooldown;
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
                cooldown = maxCooldown;

                var cmd = new CastAbility();
                cmd.spellRef = getClass().getSimpleName();
                cmd.sourceEid = source.getEid();

                cmd.target = game.camera.unproject(new Vec2(input.getMouseX(), input.getMouseY()));

                game.send(cmd);
            }
        }
    }
}
