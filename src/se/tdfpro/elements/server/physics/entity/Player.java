package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.net.command.client.CastSpell;
import se.tdfpro.elements.net.command.client.MoveCommand;
import se.tdfpro.elements.net.command.server.CreatePlayer;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

import java.util.List;

public class Player extends Circle {
    private static final List<Color> playerColors = List.of(Color.green, Color.cyan, Color.pink, Color.blue);
    private Vec2 impulse = Vec2.ZERO;
    private int controller;
    private float fireballCD = 0f;

    public Player(Vec2 position, Vec2 velocity, int controller) {
        super(position, velocity, 0.5f, Materials.PLAYER, 30f);
        this.controller = controller;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(playerColors.get(controller % playerColors.size()));
        super.draw(g);
    }

    @Override
    public void updateServer(GameServer game, float delta) {
        velocity = velocity.add(impulse);
        impulse = Vec2.ZERO;
        super.updateServer(game, delta);
    }

    @Override
    public void updateClient(GameContainer gc, GameClient game, float delta) {
        super.updateClient(gc, game, delta);
        if (controller == game.getPid()) {
            Input input = gc.getInput();
            var movement = new Vec2(0, 0);
            if (input.isKeyDown(Input.KEY_W)) {
                movement = movement.add(Vec2.UP);
            }
            if (input.isKeyDown(Input.KEY_S)) {
                movement = movement.add(Vec2.DOWN);
            }
            if (input.isKeyDown(Input.KEY_A)) {
                movement = movement.add(Vec2.LEFT);
            }
            if (input.isKeyDown(Input.KEY_D)) {
                movement = movement.add(Vec2.RIGHT);
            }

            if (movement.length2() != 0) {
                movement = movement.norm();
                game.send(new MoveCommand(getID(), movement));
            }

            if (input.isMouseButtonDown(0) && fireballCD <= 0) {
                fireballCD = 1.5f;
                var lookdir = game.camera.unproject(new Vec2(input.getMouseX(), input.getMouseY())).sub(position).norm();
                game.send(new CastSpell(lookdir, getEid()));
            }
            fireballCD -= delta;
        }
        //facing = (float) mouse.theta();
    }

    public Vec2 getImpulse() {
        return impulse;
    }

    public void setImpulse(Vec2 impulse) {
        this.impulse = impulse;
    }

    public int getController() {
        return controller;
    }

    @Override
    public ServerCommand makeCreateCommand() {
        return new CreatePlayer(this);
    }
}
