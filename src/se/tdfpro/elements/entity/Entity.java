package se.tdfpro.elements.entity;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Entity {
    private int id = -1;
    protected final List<Entity> children = new ArrayList<>();
    private int parent = -1;


    public void setParent(int parent) {
        if (this.parent == -1 || this.parent == parent) {
            this.parent = parent;
        } else {
            throw new IllegalStateException("Parent set twice");
        }
    }

    public void setId(int id) {
        if (this.id == -1 || this.id == id) {
            this.id = id;
        } else {
            throw new IllegalStateException("Id set twice");
        }
    }

    public abstract void encodeConstructorParams(Encoder encoder);

    public Stream<Entity> tree() {
        // dfs tree walk
        return Stream.concat(Stream.of(this), children.stream().flatMap(Entity::tree));
    }

    public int getId() {
        return id;
    }

    public void init(GameClient game) { }

    public void init(GameServer game) { }

    public void addChild(Entity child) {
        child.setParent(id);
        children.add(child);
    }

    public void removeChild(Entity ent) {
        // linear search, maybe better to replace list with a map ?
        // uncommon operation so probably fine
        children.remove(ent);
    }

    public void render(GameClient game, Graphics g) {
        g.pushTransform();
        draw(game, g);
        children.forEach(c -> c.render(game, g));
        g.popTransform();
    }

    protected void draw(GameClient game, Graphics g) { }

    public void update(GameClient game, float delta) {
        children.forEach(c -> c.update(game, delta));
    }

    public void update(GameServer game, float delta) {
        children.forEach(c -> c.update(game, delta));
    }

    public void destroy(GameClient game) {
        children.forEach(c -> c.destroy(game));
    }

    public void destroy(GameServer game) {
        children.forEach(c -> c.destroy(game));
    }

    public int getParent() {
        return parent;
    }


}
