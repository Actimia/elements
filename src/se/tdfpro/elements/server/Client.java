package se.tdfpro.elements.server;

import se.tdfpro.elements.server.command.ClientCommand;
import se.tdfpro.elements.server.engine.Game;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client extends Thread {
    private Socket socket;
    private Game game;

    public Client(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    public void send(ClientCommand com) {

    }

    //public void receive(ClientCommand com) {
//        com.execute(game);
    //}

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

    }
}
