package se.tdfpro.elements.server;

import se.tdfpro.elements.server.commands.ClientCommand;
import se.tdfpro.elements.server.engine.Game;

import java.net.Socket;

public class Client {
    private Socket socket;
    private Game game;

    public Client(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    public void send(ClientCommand com) {

    }

    public void receive(ClientCommand com) {
//        com.execute(game);
    }
}
