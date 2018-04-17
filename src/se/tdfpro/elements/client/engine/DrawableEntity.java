package se.tdfpro.elements.client.engine;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class DrawableEntity implements Entity {

    public Vector2f position = new Vector2f(0,0);
    public float facing = 0;

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        g.pushTransform();

        g.translate(position.x, position.y);
        g.rotate(0,0, facing);

        draw(g);

        g.popTransform();
    }

    @Override
    public void renderInterface(GameContainer gc, StateBasedGame game, Graphics g) {
        drawInterface(g);
    }

    public abstract void draw(Graphics g);

    public abstract void drawInterface(Graphics g);
}
