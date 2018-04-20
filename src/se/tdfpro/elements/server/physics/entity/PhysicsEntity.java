package se.tdfpro.elements.server.physics.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.command.Encoder;
import se.tdfpro.elements.server.GameServer;
import se.tdfpro.elements.server.physics.Material;
import se.tdfpro.elements.server.physics.Vec2;

public abstract class PhysicsEntity implements ServerEntity, ClientEntity {
    private static int nextEid;
    protected int eid = nextEid++;
    protected Vec2 position;

    protected final Material material;

    public PhysicsEntity(Vec2 position, Material material) {
        this.position = position;
        this.material = material;
    }

    public void init(GameClient game) {

    }

    @Override
    public void render(GameContainer gc, GameClient game, Graphics g) {
        g.pushTransform();

        var pos = getPosition();
        g.translate(pos.x, pos.y);
        g.rotate(0, 0, getVelocity().theta());

        setupGraphics(g);

        draw(game, g);

        g.popTransform();
    }

    void setupGraphics(Graphics g) {
        g.setColor(material.getColor());
    }

    public void onCollide(GameServer game, PhysicsEntity other) {

    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getVelocity() {
        return Vec2.ZERO;
    }

    public void setVelocity(Vec2 velocity) {
        // silently ignore for static objects.
    }

    public boolean isDynamic() {
        return false;
    }

    public int getEid() {
        return eid;
    }

    public float getInvMass() {
        return 0;
    }

    public Material getMaterial() {
        return material;
    }

    public void changeVelocity(Vec2 delta) {

    }

    public void changePosition(Vec2 delta) {
        position = position.add(delta);
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    @Override
    public abstract void encodeConstructorParams(Encoder encoder);
}
