package se.tdfpro.elements.client.engine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface Entity {
    void render(GameContainer gc, StateBasedGame game, Graphics g);
    void renderInterface(GameContainer gc, StateBasedGame game, Graphics g);
    boolean update(GameContainer gc, StateBasedGame game, int delta);
}
