package se.tdfpro.elements.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.client.engine.DrawableEntity;

public class Spell extends DrawableEntity {
    private Image icon;

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void drawInterface(Graphics g) {

    }

    @Override
    public boolean update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        return false;
    }
}
