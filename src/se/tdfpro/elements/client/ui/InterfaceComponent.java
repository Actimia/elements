package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.ClientEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class InterfaceComponent implements ClientEntity {
    protected Vec2 position;
    protected Color color = Color.white;
    protected final List<InterfaceComponent> children = new ArrayList<>();

    public InterfaceComponent(Vec2 pos) {
        this.position = pos;
    }

    @Override
    public void render(GameContainer gc, GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        g.setColor(color);
        draw(game, g);
        // children are drawn in their parents transform, enabling easy composition
        children.forEach(comp -> comp.render(gc, game, g));
        g.popTransform();
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {
        updateUI(gc, game, delta);
        children.forEach(comp -> comp.updateClient(gc, game, delta));
    }

    protected void updateUI(GameContainer gc, GameClient game, float delta) {

    }

    public Vec2 getPosition() {
        return position;
    }

    public InterfaceComponent setPosition(Vec2 position) {
        this.position = position;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public InterfaceComponent setColor(Color color) {
        this.color = color;
        return this;
    }
}
