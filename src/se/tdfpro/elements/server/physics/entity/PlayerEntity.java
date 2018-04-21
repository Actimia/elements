package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.DecodeConstructor;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.command.client.PlayerMove;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.Vec2;

public class PlayerEntity extends Circle {
    private Vec2 impulse = Vec2.ZERO;
    private final int controller;
    private final String username;
    private final Color color;

    @DecodeConstructor
    public PlayerEntity(Vec2 position, Vec2 velocity, int controller, String username, Color color) {
        super(position, velocity, 0.5f, Material.PLAYER, 30f);
        this.controller = controller;
        this.username = username;
        this.color = color;
    }

    @Override
    public void init(GameClient game) {
        if (getController() == game.getPid()) {
            game.initialiseInterface(this);
        }
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {
        encoder.encode(position);
        encoder.encode(velocity);
        encoder.encode(controller);
        encoder.encode(username);
        encoder.encode(color);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        super.draw(game, g);
        g.rotate(0, 0, -getVelocity().theta());
        g.drawString(username, -g.getFont().getWidth(username) / 2, radius + 10);
    }

    @Override
    public void onUpdate(GameServer game, float delta) {
        velocity = velocity.add(impulse);
        impulse = Vec2.ZERO;
        super.onUpdate(game, delta);
    }

    @Override
    public void onUpdate(GameClient game, float delta) {
        super.onUpdate(game, delta);
        if (controller == game.getPid()) {
            Input input = game.getInput();
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
                game.send(new PlayerMove(getId(), movement));
            }

//            game.camera.centerOn(this.position);
        }
        //facing = (float) mouse.theta();

    }

    @Override
    public Color getColor() {
        return color;
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

    public String getUsername() {
        return username;
    }
}
