package se.tdfpro.elements.server.physics;

import se.tdfpro.elements.server.GameServer;

public abstract class PhysicsEntity2 {
    private static int nextid;

    private final int eid = nextid++;
    public Vec2 position;
    public Vec2 velocity;

    public final float invMass;
    public final Material material;

    public PhysicsEntity2(Vec2 position, Vec2 velocity, float invMass, Material material) {
        this.position = position;
        this.velocity = velocity;
        this.invMass = invMass;
        this.material = material;
    }

    public void update(GameServer game, float delta){
        position = position.add(velocity.scale(delta));
        velocity = velocity.scale(0.95f);

//        for (PhysicsEntity2 ent : game.getEntities2().values()) {
//            if(ent.eid != this.eid) {

//                var limit = radius + ent.radius;
//                var normal = position.sub(ent.position);
//                if (normal.length2() < limit * limit) {
//                    resolveCollision(ent, normal);
//                }
//            }
//        }
//        game.broadcast(new UpdateEntity(this));
    }



}
