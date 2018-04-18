package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.command.CommandQueue;
import se.tdfpro.elements.server.command.ServerCommand;
import se.tdfpro.elements.server.command.server.PlayerDisconnect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Network {
    public static final byte[] MAGIC_SEQUENCE = {1, 3, 3, 7};

    private static int nextpid = 0;

    private ServerSocket server;

    private ConcurrentHashMap<Integer, ServerClient> clients = new ConcurrentHashMap<>();
    private CommandQueue<ClientCommand> commands = new CommandQueue<>();

    private Executor threads = Executors.newCachedThreadPool();

    public Network(int port) throws IOException {
        server = new ServerSocket(port);
        threads.execute(this::listen);
        System.out.println("Listening on " + port);
    }

    public void broadcast(ServerCommand command) {
        clients.values().forEach(client -> threads.execute(() -> client.send(command)));
    }

    public void send(int pid, ServerCommand command) {
        threads.execute(() -> clients.get(pid).send(command));
    }

    public List<ClientCommand> getCommands() {
        return commands.getAll();
    }

    public void disconnectClient(int pid) {
        var client = clients.get(pid);
        client.disconnect();
        clients.remove(pid);

        PlayerDisconnect playerDisconnect = new PlayerDisconnect();
        playerDisconnect.playerid = pid;
        System.out.println(pid + " disconnected.");
        broadcast(playerDisconnect);
    }

    private void listen() {
        while (true) {
            try {
                Socket sock = server.accept();
                sock.setTcpNoDelay(true);

                int pid = nextpid++;
                ServerClient client = new ServerClient(this, sock, commands::onReceive, pid);
                clients.put(pid, client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Executor getExecutor() {
        return threads;
    }
}
