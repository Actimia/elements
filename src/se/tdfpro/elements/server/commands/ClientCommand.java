package se.tdfpro.elements.server.commands;

import se.tdfpro.elements.server.engine.Game;
import se.tdfpro.elements.server.engine.Vec2;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class ClientCommand { // from client

    private static HashMap<String, Class<? extends ClientCommand>> commands;
    public static void register(Class<? extends ClientCommand> command) {
        var name = command.getCanonicalName();

        if(commands.containsKey(name)) {
            throw new RuntimeException(name + " is already registered");
        }
        commands.put(name, command);
    }

    public byte[] encode() {
        Encoder encoder = new Encoder();
        encoder.encode(this.getClass().getName());
        Arrays.stream(this.getClass().getFields())
            .filter(f -> f.getAnnotation(Send.class) != null)
            .forEach(f -> encoder.encode(this, f));

//        return encoder.getBytes();
        return null;
    }

    public ClientCommand decode(byte[] bytes){
        return null;
    }


    @Send
    public int player;

    abstract void execute(Game game);
}


class MoveCommand extends ClientCommand {
    static {
        ClientCommand.register(MoveCommand.class);
    }

    @Send
    public Vec2 velo;

    @Send
    public String something;

    @Override
    public void execute(Game game) {
        game.getEntities();
    }
}

