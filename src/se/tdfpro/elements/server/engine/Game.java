package se.tdfpro.elements.server.engine;

import se.tdfpro.elements.server.Client;
import se.tdfpro.elements.server.ElementsServer;
import se.tdfpro.elements.server.commands.Command;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Game {
    public static final int TICKS = 20;
    public static final int TICK_TIME = 1000/TICKS;

    private long lastTickStart = 0;

    private ElementsServer networking;
    private List<Entity> entities = new ArrayList<>();

    public Game(ElementsServer networking) {

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

    public void spawnEntity(Entity ent) {
        entities.add(ent);
    }

    public void executeCommands() {
        var commands = networking.getCommands();
        commands.forEach(cmd -> cmd.execute(this));
    }

    public void update(float delta) {
        entities.forEach(ent -> ent.update(this, delta));
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
