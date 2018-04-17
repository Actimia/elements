package se.tdfpro.elements.client.engine.entity;

import org.newdawn.slick.*;
import se.tdfpro.elements.client.GameClient;

public class Player extends DrawableEntity {
    public static final int RADIUS = 30;
    public static final int DIAMETER = 2 * RADIUS;

    private String name;


    public Player(int id, String username) {
        super(id);
        name = username;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setLineWidth(3);
        g.drawOval(-RADIUS, -RADIUS, DIAMETER, DIAMETER);
        g.drawLine(0,0, RADIUS,0);

        var f = g.getFont();
        var width = f.getWidth(name);
        g.drawString(name, -width/2, -50);
    }

    public void drawInterface(Graphics g) {
        g.drawString("Hello interface", 800, 800);
    }

    @Override
    public boolean update(GameContainer gc, GameClient game, int delta) {
        position.add(velocity.copy().scale(delta/1000f));
        return false;
    }
}
