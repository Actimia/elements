package se.tdfpro.elements.client;

import org.newdawn.slick.util.Log;

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
