package se.tdfpro.elements.server.physics.shapes;

import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Materials;
import se.tdfpro.elements.server.physics.Vec2;

public class Player extends Circle {

    public Vec2 impulse = Vec2.ZERO;

    public Player(Vec2 position, Vec2 velocity) {
        super(position, velocity, 0.5f, Materials.PLAYER, 30f);
    }

    @Override
    public void update(GameServer game, float delta) {
        velocity = velocity.add(impulse);
        impulse = Vec2.ZERO;
        super.update(game, delta);
    }
}
