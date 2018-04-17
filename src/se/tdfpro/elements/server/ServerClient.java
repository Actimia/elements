package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerClient {
    private Network network;
    private Thread listenThread;
    private Socket socket;
    private CommandQueue<ClientCommand> commands;
    private boolean isConnected = true;
    private int id;

    public ServerClient(Network network, Socket socket, CommandQueue<ClientCommand> commands, int id) {
        this.network = network;
        this.socket = socket;
        this.commands = commands;
        this.id = id;

        this.listenThread = new Thread(this::listen);
        this.listenThread.start();
    }

    public void disconnect() {
        isConnected = false;

        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(ServerCommand com) {
        var encoded = Encoder.encodeCommand(com);
        int len = encoded.length;
        byte[] headerLengthVal = {
                (byte) (len >> 24),
                (byte) (len >> 16),
                (byte) (len >> 8),
                (byte) len
        };
        try {
            var out = this.socket.getOutputStream();
            out.write(Network.MAGIC_SEQUENCE);
            out.write(headerLengthVal);
            out.write(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        int HEADER_LENGTH = 8;

        try {
            InputStream in = this.socket.getInputStream();
            byte[] headerBuffer = new byte[HEADER_LENGTH];

            while (isConnected) {
                int header_read = 0;
                while(header_read < HEADER_LENGTH) {
                    header_read += in.read(headerBuffer, header_read, HEADER_LENGTH - header_read);
                }

                if (header_read != HEADER_LENGTH) throw new RuntimeException("Incomplete header");
                for (int i = 0; i<4; i++) {
                    if (headerBuffer[i] != Network.MAGIC_SEQUENCE[i]){
                        throw new RuntimeException("Bad magic sequence");
                    }
                }

                int length = headerBuffer[4] << 24
                        | headerBuffer[5] << 16
                        | headerBuffer[6] << 8
                        | headerBuffer[7];

                byte[] commandBuffer = new byte[length];
                int cmd_read = 0;
                while(cmd_read < length) {
                    cmd_read += in.read(commandBuffer, cmd_read, length - cmd_read);
                }

                if (cmd_read < length) throw new RuntimeException("Read less than indicated # of bytes");

                ClientCommand comm = Decoder.decode(commandBuffer);
                comm.pid = id;
                commands.onReceive(comm);
            }
        } catch (IOException e) {
            e.printStackTrace();
            network.disconnectClient(id);
        }
    }
}
