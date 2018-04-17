package se.tdfpro.elements.client.engine.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;

public interface Entity {
    void render(GameContainer gc, GameClient game, Graphics g);
    void renderInterface(GameContainer gc, GameClient game, Graphics g);
    boolean update(GameContainer gc, GameClient game, int delta);

    int getID();
}
