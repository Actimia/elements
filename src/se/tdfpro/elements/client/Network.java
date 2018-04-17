package se.tdfpro.elements.client;

import org.newdawn.slick.util.Log;
import se.tdfpro.elements.server.command.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Network extends Thread{

    public static final byte[] MAGIC_SEQUENCE = { 1, 3, 3, 7 };
    private Socket socket;
    private final OutputStream out;
    private CommandQueue<ServerCommand> commands = new CommandQueue<>();

    public Network(String host, int port) throws IOException {
        Log.info("Connecting to " + host + "@" + port + "...");
        socket = new Socket(host, port);
        out = socket.getOutputStream();
        socket.setTcpNoDelay(true);

        Log.info("Connected");

    }

    public void send(Command com) {
        var encoded = Encoder.encodeCommand(com);
        int len = encoded.length;
        byte[] headerLengthVal = {
                (byte) (len >> 24),
                (byte) (len >> 16),
                (byte) (len >> 8),
                (byte) len
        };
        try {
            synchronized (out) {
                out.write(MAGIC_SEQUENCE);
                out.write(headerLengthVal);
                out.write(encoded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int HEADER_LENGTH = 8;

        try {
            InputStream in = this.socket.getInputStream();
            byte[] headerBuffer = new byte[HEADER_LENGTH];

            while (true) {
                int header_read = 0;
                while(header_read < HEADER_LENGTH) {
                    header_read += in.read(headerBuffer, header_read, HEADER_LENGTH - header_read);
                }
                if (header_read != HEADER_LENGTH) throw new RuntimeException("Incomplete header");
                for (int i = 0; i<4; i++) {
                    if (headerBuffer[i] != MAGIC_SEQUENCE[i]){
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
                    System.out.println(cmd_read);
                }

                if (cmd_read < length) throw new RuntimeException("Read less than indicated # of bytes");
                commands.onReceive(Decoder.decode(commandBuffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<ServerCommand> getCommands() {
        return commands.getAll();
    }
}
