package se.tdfpro.elements.entity;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Entity {
    private int id = -1;
    private final List<Entity> children = new ArrayList<>();
    protected Vec2 position;

    public Entity() {
        this(Vec2.ZERO);
    }

    public Entity(Vec2 position) {
        this.position = position;
    }

    public void setId(int id) {
        if (id == -1) {
            this.id = id;
        } else {
            throw new IllegalStateException("Id set twice");
        }
    }

    public abstract void encodeConstructorParams(Encoder encoder);

    public Stream<Entity> tree() {
        return Stream.concat(Stream.of(this), children.stream().flatMap(Entity::tree));
    }

    public int getId() {
        return id;
    }

    public void init(GameClient game) {
        game.addEntity(this);
    }

    public void init(GameServer game) {

    }

    public void addChild(Entity child) {
        children.add(child);
    }

    public void render(GameClient game, Graphics g) {
        g.pushTransform();
        g.translate(position.x, position.y);
        draw(game, g);
        children.forEach(c -> c.render(game, g));
        g.popTransform();
    }

    protected void draw(GameClient game, Graphics g) {

    }

    public void onUpdate(GameClient game, float delta){

    }


    public void onUpdate(GameServer game, float delta){

    }

    public void onDestroy(GameClient game) {

    }

    public void onDestroy(GameServer game) {

    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }
}
