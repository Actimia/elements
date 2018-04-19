package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;

public interface ClientEntity {
    void render(GameContainer gc, GameClient game, Graphics g);

    void draw(Graphics g);

    void updateClient(GameContainer gc, GameClient game, float delta);

}
