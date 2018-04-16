package se.tdfpro.elements.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ElementsServer {

    private ServerSocket server;
    private List<Client> clients = new ArrayList<>();


    public ElementsServer(int port) throws IOException {
        server = new ServerSocket(port);

    }

    public void listen() throws IOException {
        while(true) {
            Socket sock = server.accept();
            sock.setTcpNoDelay(true);
            clients.add(new Client(sock));
        }
    }

    public static void main(String[] args) {
        try {
            new ElementsServer(7777).listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
