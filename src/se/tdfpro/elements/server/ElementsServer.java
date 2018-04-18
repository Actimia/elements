package se.tdfpro.elements.server;

import java.io.IOException;

public class ElementsServer {

    private GameServer game;

    public ElementsServer(int port) throws IOException {
        game = new GameServer(port);
        game.run();
    }

    public static void main(String[] args) {
        try {
            new ElementsServer(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
