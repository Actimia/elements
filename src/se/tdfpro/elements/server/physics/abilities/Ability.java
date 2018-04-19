package se.tdfpro.elements.server.physics.abilities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.client.CastAbility;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.ClientEntity;
import se.tdfpro.elements.server.physics.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Ability implements ClientEntity {
    private static final Map<String, SpawnAbility> abilities = new HashMap<>();

    public static void registerAbility(Class<? extends Ability> cls, SpawnAbility effect) {
        abilities.put(cls.getSimpleName(), effect);
    }

    public static SpawnAbility getAbility(String ref) {
        return abilities.get(ref);
    }

    private Player source;
    private Vec2 position;
    private float cooldown;
    private float maxCooldown;
    private Predicate<Input> keybind;

    public Ability(Player player, Vec2 position, Predicate<Input> keybind, float maxCooldown) {
        this.source = player;
        this.position = position;
        this.maxCooldown = maxCooldown;
        this.keybind = keybind;
    }

    @Override
    public void render(GameContainer gc, GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        draw(g);
        g.popTransform();
    }

    @Override
    public void draw(Graphics g) {
        var size = 40f;
        g.setColor(Color.white);
        g.drawRect(0,0, size, size);
        var cd = 1 - Math.max(cooldown/maxCooldown, 0);
        g.setColor(new Color(1f, 1f,1f,0.3f));
        g.fillRect(0, cd * size, size, (1f-cd) * size);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta){
        this.cooldown -= delta;
        var input = gc.getInput();
        if(keybind.test(input)) {
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
