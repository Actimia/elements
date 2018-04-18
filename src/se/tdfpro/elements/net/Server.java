package se.tdfpro.elements.net;

import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.ServerCommand;

import java.util.List;

public interface Server extends NetworkConstants {
    void broadcast(ServerCommand com);
    void send(int pid, ServerCommand com);

    List<ClientCommand> getCommands();
}
