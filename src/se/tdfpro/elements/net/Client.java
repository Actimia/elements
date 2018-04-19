package se.tdfpro.elements.net;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.ServerCommand;

import java.util.List;

public interface Client extends NetworkConstants {
    void send(ClientCommand com);
    void accept(ServerCommand com);
    List<ServerCommand> getCommands();
}
