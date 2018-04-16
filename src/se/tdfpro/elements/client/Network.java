package se.tdfpro.elements.client;

import org.newdawn.slick.util.Log;

import java.io.IOException;
import java.net.Socket;

public class Network {
    private Socket socket;
    public Network(String host, int port) throws IOException {
        Log.info("Connecting to " + host + "@" + port + "...");
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);

    }
}
