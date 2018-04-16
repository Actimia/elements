package se.tdfpro.elements.client;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.client.engine.Camera;
import se.tdfpro.elements.client.engine.DrawableEntity;

public class Player extends DrawableEntity {
    public static final int RADIUS = 30;
    public static final int DIAMETER = 2 * RADIUS;

    private Camera camera;

    public Player(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setLineWidth(3);
        g.drawOval(-RADIUS, -RADIUS, DIAMETER, DIAMETER);
        g.drawLine(0,0, RADIUS,0);
    }

    public void drawInterface(Graphics g) {
        g.drawString("Hello interface", 800, 800);
    }

    @Override
    public boolean update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input input = gc.getInput();
        var movement = new Vector2f();
        if (input.isKeyDown(Input.KEY_W)) {
            movement.add(new Vector2f(0, -1));
        }
        if (input.isKeyDown(Input.KEY_S)) {
            movement.add(new Vector2f(0, 1));
        }
        if (input.isKeyDown(Input.KEY_A)) {
            movement.add(new Vector2f(-1, 0));
        }
        if (input.isKeyDown(Input.KEY_D)) {
            movement.add(new Vector2f(1, 0));
        }

        if (movement.lengthSquared() != 0){
            movement.normalise();
            movement.scale(250 * delta/1000f);
            position.add(movement);
        }

        var mouse = camera.unproject(new Vector2f(input.getMouseX(), input.getMouseY()));
        mouse.sub(position);
        facing = (float) mouse.getTheta();
        return false;
    }
}
