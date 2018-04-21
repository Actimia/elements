package se.tdfpro.elements.entity;

import org.newdawn.slick.Color;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Box;
import se.tdfpro.elements.server.physics.Vec2;
import se.tdfpro.elements.server.physics.entity.PlayerEntity;
import se.tdfpro.elements.server.physics.entity.Ray;

public class World extends Entity {

    @Override
    public void init(GameServer game) {
        var origin = new Vec2(0, 0);
        var area = new Vec2(1600, 1000);
        var playArea = new Box(origin, area);

        addChild(game.spawnEntity(new Ray(playArea.topLeft(), Vec2.RIGHT)));
        addChild(game.spawnEntity(new Ray(playArea.topRight(), Vec2.DOWN)));
        addChild(game.spawnEntity(new Ray(playArea.bottomRight(), Vec2.LEFT)));
        addChild(game.spawnEntity(new Ray(playArea.bottomLeft(), Vec2.UP)));

        addChild(game.spawnEntity(new PlayerEntity(new Vec2(800, 600), Vec2.ZERO, -1, "", Color.white)));
        addChild(game.spawnEntity(new PlayerEntity(new Vec2(400, 600), new Vec2(20, 0), -1, "", Color.white)));
    }

    @Override
    public void encodeConstructorParams(Encoder encoder) {

    }

    @Override
    public void onUpdate(GameClient game, float delta) {

    }

    @Override
    public void onUpdate(GameServer game, float delta) {

    }
}
