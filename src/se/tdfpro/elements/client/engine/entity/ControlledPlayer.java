package se.tdfpro.elements.client.engine.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.client.engine.Camera;
import se.tdfpro.elements.server.command.client.MoveCommand;

public class ControlledPlayer extends Player{


    public ControlledPlayer(int id, String name) {
        super(id, name);
    }

    @Override
    public boolean update(GameContainer gc, GameClient game, int delta) {
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
            game.send(new MoveCommand(getID(), movement));
        }

        var mouse = game.camera.unproject(new Vector2f(input.getMouseX(), input.getMouseY()));
        mouse.sub(position);
        facing = (float) mouse.getTheta();
        return false;
    }
}
