package se.tdfpro.elements.server;

import java.net.Socket;

public class Client {
    private Socket socket;
    public Client(Socket socket) {
        this.socket = socket;
    }
}
