package se.tdfpro.elements.net;

import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.ServerCommand;
import se.tdfpro.elements.net.command.server.PlayerDisconnect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InternetServer implements Server {
    private static int nextPid = 0;

    private ServerSocket server;

    private ConcurrentHashMap<Integer, ServerClient> clients = new ConcurrentHashMap<>();

    private Executor threads = Executors.newCachedThreadPool();
    private final CommandQueue<ClientCommand> inbox = new CommandQueue<>();

    public InternetServer(int port) throws IOException {
        server = new ServerSocket(port);
        threads.execute(this::listen);
        System.out.println("Listening on " + port);
    }

    @Override
    public void broadcast(ServerCommand command) {
        clients.values().forEach(client -> threads.execute(() -> client.send(command)));
    }

    @Override
    public void send(int pid, ServerCommand command) {
        threads.execute(() -> clients.get(pid).send(command));
    }

    @Override
    public List<ClientCommand> getCommands() {
        return inbox.getCommands();
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

                int pid = nextPid++;
                var client = new InternetServerClient(this, sock, inbox::accept, pid);
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
