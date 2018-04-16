package se.tdfpro.elements.client;

import org.newdawn.slick.util.Log;
import se.tdfpro.elements.server.command.Command;
import se.tdfpro.elements.server.command.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Network extends Thread{
    private Socket socket;
    public Network(String host, int port) throws IOException {
        Log.info("Connecting to " + host + "@" + port + "...");
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);

        System.out.println("Connected");

        OutputStream out = socket.getOutputStream();

        byte[] buf = {0, 0, 0, 0, 0, 0, 0, 2, 4, 2, 0 };


        while (true) {
            System.out.println("Sending bytes");
            out.write(buf);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) { }
        }
    }

    public void send(Command com) {
        var encoder = new Encoder();
        encoder.encode(com);
        var encoded = encoder.getBytes();

        byte[] MAGIC_SEQUENCE = { 0, 0, 0, 0 };
        int len = encoded.length;
        byte[] headerLengthVal = {
                (byte) (len >> 24),
                (byte) (len >> 16),
                (byte) (len >> 8),
                (byte) len
        };

        try {
            var out = this.socket.getOutputStream();
            out.write(MAGIC_SEQUENCE);
            out.write(headerLengthVal);
            out.write(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int HEADER_LENGTH = 8;

        try {
            InputStream in = this.socket.getInputStream();
            byte[] headerBuffer = new byte[8];

            while (true) {
                int read = in.read(headerBuffer, 0, HEADER_LENGTH);

                if (read != HEADER_LENGTH) throw new Error("Incomplete header");

                int length = headerBuffer[4] << 24
                        | headerBuffer[5] << 16
                        | headerBuffer[6] << 8
                        | headerBuffer[7];

                byte[] commandBuffer = new byte[length];
                read = in.read(commandBuffer, 0, length);

                if (read < length) throw new Error("Read less than indicated # of bytes");

                System.out.println("Read" + read + " bytes");
                System.out.println(Arrays.toString(commandBuffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }}
