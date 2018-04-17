package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.CommandQueue;
import se.tdfpro.elements.server.command.ServerCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Network {
    private ServerSocket server;

    private List<ServerClient> clients = new ArrayList<>();
    private CommandQueue<ClientCommand> commands = new CommandQueue<>();

    private Executor senders = Executors.newCachedThreadPool();

    public Network(int port) throws IOException {
        server = new ServerSocket(port);
        new Thread(this::listen).start();
    }

    public void broadcast(ServerCommand command) {
        clients.forEach(client -> senders.execute(() -> client.send(command)));
    }

    public void send(int pid, ServerCommand command) {
        senders.execute(() -> clients.get(pid).send(command));
    }

    public List<ClientCommand> getCommands() {
        return commands.getAll();
    }

    private void listen() {
        System.out.println("Listening on 7777");
        while(true) {
            try {
                Socket sock = server.accept();
                sock.setTcpNoDelay(true);

                // TODO: handle dropped connections
                ServerClient client = new ServerClient(sock, commands, clients.size());
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
