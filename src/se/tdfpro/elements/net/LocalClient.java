package se.tdfpro.elements.net;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.ServerCommand;

import java.util.List;
import java.util.function.Consumer;

public class LocalClient implements ServerClient, Client {
    private final CommandQueue<ServerCommand> inbox = new CommandQueue<>();
    private final int pid;
    private final Consumer<ClientCommand> onClientCommand;

    public LocalClient(int pid, Consumer<ClientCommand> onClientCommand) {
        this.pid = pid;
        this.onClientCommand = onClientCommand;
    }

    @Override
    public void send(ClientCommand com) {
        accept(com);
    }

    @Override
    public void accept(ServerCommand com) {
        inbox.accept(com);
    }

    @Override
    public List<ServerCommand> getCommands() {
        return inbox.getCommands();
    }

    @Override
    public void accept(ClientCommand com) {
        com.pid = pid;
        onClientCommand.accept(com);
    }

    @Override
    public void send(ServerCommand com) {
        accept(com);
    }

    @Override
    public void disconnect() {

    }
}
