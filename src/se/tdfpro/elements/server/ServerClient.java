package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class ServerClient {
    private Network network;
    private Socket socket;
    private final OutputStream out;
    private Consumer<ClientCommand> onCommand;
    private boolean isConnected = true;
    private int id;
    private BlockingQueue<ServerCommand> sendQueue = new LinkedBlockingQueue<>();

    private static final int HEADER_LENGTH = 8;

    public ServerClient(Network network, Socket socket, Consumer<ClientCommand> onCommand, int id) throws IOException {
        this.network = network;
        this.socket = socket;
        this.onCommand = onCommand;
        this.id = id;

        out = socket.getOutputStream();

        network.getExecutor().execute(this::listen);
        network.getExecutor().execute(this::doSend);
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
        sendQueue.add(com);
    }

    public void doSend() {
        while(isConnected) {
            try {
                ServerCommand com = sendQueue.take();
                var encoded = Encoder.encodeCommand(com);
                int len = encoded.length;
                byte[] headerLength = {
                        (byte) (len >> 24),
                        (byte) (len >> 16),
                        (byte) (len >> 8),
                        (byte) len
                };

                out.write(Network.MAGIC_SEQUENCE);
                out.write(headerLength);
                out.write(encoded);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                network.disconnectClient(id);
            }
        }
    }

    public void listen() {

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
                onCommand.accept(comm);
            }
        } catch (IOException e) {
            network.disconnectClient(id);
        }
    }
}
