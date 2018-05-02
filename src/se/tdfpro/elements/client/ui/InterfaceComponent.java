package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.entity.Entity;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.util.Vec2;


public abstract class InterfaceComponent extends Entity {
    protected Vec2 position;
    protected Color color = Color.white;

    public InterfaceComponent(Vec2 position) {
        this.position = position;
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {

    }

    @Override
    public void render(GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        draw(game, g);
        children.forEach(c -> c.render(game, g));
        g.popTransform();
    }

    @Override
    public void update(GameClient game, float delta) {
        super.update(game, delta);
    }

    public Color getColor() {
        return color;
    }

    public InterfaceComponent setColor(Color color) {
        this.color = color;
        return this;
    }
}
