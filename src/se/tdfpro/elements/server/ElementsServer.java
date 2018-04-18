package se.tdfpro.elements.server;

import se.tdfpro.elements.net.InternetServer;

import java.io.IOException;

public class ElementsServer {

    public static void main(String[] args) {
        try {
            var game = new GameServer(new InternetServer(7777));
            game.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
