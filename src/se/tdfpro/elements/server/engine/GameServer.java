package se.tdfpro.elements.server.engine;

import se.tdfpro.elements.server.ElementsServer;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.HandshakeReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class GameServer {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000/TICKS;

    private long lastTickStart = 0;

    private ElementsServer networking;

    private Map<Integer, PhysicsEntity> entities = new HashMap<>();

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
        entities.put(ent.id, ent);
    }

    public void executeCommands() {
        var commands = networking.getCommands();

        commands.forEach(cmd -> cmd.execute(this));
    }

    public void update(float delta) {
        entities.values().forEach(ent -> ent.update(this, delta));
    }

    public Map<Integer, PhysicsEntity> getEntities() {
        return entities;
    }

    public void send(int pid, ServerCommand command) {
        networking.send(pid, command);
    }
}
