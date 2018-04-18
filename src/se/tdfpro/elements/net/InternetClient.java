package se.tdfpro.elements.net;

import org.newdawn.slick.util.Log;
import se.tdfpro.elements.net.command.ClientCommand;
import se.tdfpro.elements.net.command.Decoder;
import se.tdfpro.elements.net.command.Encoder;
import se.tdfpro.elements.net.command.ServerCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InternetClient implements Client {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private final Executor threads = Executors.newCachedThreadPool();
    private final CommandQueue<ClientCommand> outbox = new CommandQueue<>();
    private final CommandQueue<ServerCommand> inbox = new CommandQueue<>();
    private boolean isConnected = true;

    public InternetClient(String host, int port) throws IOException {
        Log.info("Connecting to " + host + "@" + port + "...");
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();

        Log.info("Connected");

        threads.execute(this::listen);
        threads.execute(this::doSend);
    }

    public void disconnect() {
        isConnected = false;
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(ClientCommand com) {
        outbox.accept(com);
    }

    @Override
    public void accept(ServerCommand com) {
        inbox.accept(com);
    }

    @Override
    public List<ServerCommand> getCommands() {
        return inbox.getCommands();
    }

    private void doSend() {
        while (isConnected) {
            try {
                ClientCommand com = outbox.getCommand();
                var encoded = Encoder.encodeCommand(com);
                int len = encoded.length;
                byte[] headerLengthVal = {
                        (byte) (len >> 24),
                        (byte) (len >> 16),
                        (byte) (len >> 8),
                        (byte) len
                };
                synchronized (out) {
                    out.write(MAGIC_SEQUENCE);
                    out.write(headerLengthVal);
                    out.write(encoded);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void listen() {
        while (isConnected) {
            try {
                byte[] headerBuffer = new byte[HEADER_LENGTH];
                int header_read = 0;
                while (header_read < HEADER_LENGTH) {
                    header_read += in.read(headerBuffer, header_read, HEADER_LENGTH - header_read);
                }
                if (header_read != HEADER_LENGTH) throw new RuntimeException("Incomplete header");
                for (int i = 0; i < 4; i++) {
                    if (headerBuffer[i] != MAGIC_SEQUENCE[i]) {
                        throw new RuntimeException("Bad magic sequence");
                    }
                }
                int length = headerBuffer[4] << 24
                        | headerBuffer[5] << 16
                        | headerBuffer[6] << 8
                        | headerBuffer[7];

                byte[] commandBuffer = new byte[length];
                int cmd_read = 0;
                while (cmd_read < length) {
                    cmd_read += in.read(commandBuffer, cmd_read, length - cmd_read);
                }

                if (cmd_read < length) throw new RuntimeException("Read less than indicated # of bytes");
                accept(Decoder.decode(commandBuffer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
