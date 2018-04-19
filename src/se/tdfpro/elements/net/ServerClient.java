package se.tdfpro.elements.net;

import se.tdfpro.elements.command.ClientCommand;
import se.tdfpro.elements.command.ServerCommand;

public interface ServerClient extends NetworkConstants {
    void accept(ClientCommand com);

    void send(ServerCommand com);

    void disconnect();
}
