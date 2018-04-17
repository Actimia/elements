package se.tdfpro.elements.client.engine.entity;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.client.GameClient;

public abstract class DrawableEntity implements Entity {

    private final int id;
    public Vector2f position = new Vector2f(0,0);
    public Vector2f velocity = new Vector2f(0,0);
    public float facing = 0;

    public DrawableEntity(int id){
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void render(GameContainer gc, GameClient game, Graphics g) {
        g.pushTransform();

        g.translate(position.x, position.y);
        g.rotate(0,0, (float) velocity.getTheta());

        draw(g);

        g.popTransform();
    }

    @Override
    public void renderInterface(GameContainer gc, GameClient game, Graphics g) {
        drawInterface(g);
    }

    public abstract void draw(Graphics g);

    public abstract void drawInterface(Graphics g);
}
