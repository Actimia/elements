package se.tdfpro.elements.server.engine;

import se.tdfpro.elements.server.ElementsServer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class GameServer {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000/TICKS;

    private long lastTickStart = 0;

    private ElementsServer networking;
    private List<PhysicsEntity> entities = new ArrayList<>();

    public GameServer(ElementsServer networking) {

        this.networking = networking;
    }

    public void run() {
        while (true){

            var delta = System.currentTimeMillis() - lastTickStart;
            lastTickStart = System.currentTimeMillis();

            executeCommands();
            update(delta);

            var frametime = System.currentTimeMillis() - lastTickStart;
            var idletime = TICK_TIME - frametime;
            if (idletime < 0) idletime = 0;
            try {
                sleep(idletime);
            } catch (InterruptedException ignored) {}
        }
    }

    public void spawnEntity(PhysicsEntity ent) {
        entities.add(ent);
    }

    public void executeCommands() {
        var commands = networking.getCommands();

        commands.forEach(cmd -> cmd.execute(this));
    }

    public void update(float delta) {
        entities.forEach(ent -> ent.update(this, delta));
    }

    public List<PhysicsEntity> getEntities() {
        return entities;
    }
}
