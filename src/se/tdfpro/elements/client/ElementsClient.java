package se.tdfpro.elements.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.net.Client;
import se.tdfpro.elements.net.InternetClient;
import se.tdfpro.elements.net.LocalServer;
import se.tdfpro.elements.server.GameServer;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ElementsClient extends StateBasedGame {

    private final Client net;

    public static void main(String[] args) {
        try {
            AppGameContainer gc = new AppGameContainer(new ElementsClient());
            gc.setDisplayMode(1600, 1000, false);
            gc.setTargetFrameRate(60);
            gc.setAlwaysRender(true);
            gc.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public ElementsClient() {
        super("Elements");

        try {
            net = new InternetClient("localhost", 7777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        var thread = Executors.newSingleThreadExecutor();
//        var localServer = new LocalServer();
//        var server = new GameServer(localServer);
//        thread.execute(server::run);
//        net = localServer.createClient();
    }

    @Override
    public void initStatesList(GameContainer gc) {
//        addState(new MainMenu(net));
        addState(new GameClient(net));
    }
}
