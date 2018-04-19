package se.tdfpro.elements.net;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.ServerCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalServer implements Server {
    private static int nextPid = 0;
    private final Map<Integer, ServerClient> clients = new HashMap<>();
    private final CommandQueue<ClientCommand> inbox = new CommandQueue<>();

    @Override
    public void send(int pid, ServerCommand com) {
        clients.get(pid).send(com);
    }

    @Override
    public List<ClientCommand> getCommands() {
        return inbox.getCommands();
    }

    public Client createClient() {
        var pid = nextPid++;
        LocalClient client = new LocalClient(pid, inbox::accept);
        clients.put(pid, client);
        return client;
    }

    @Override
    public void broadcast(ServerCommand com) {
        clients.values().forEach(c -> c.send(com));
    }
}
