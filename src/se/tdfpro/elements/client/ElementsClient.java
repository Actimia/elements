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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ElementsClient extends StateBasedGame {
    private static final Path configFile = Paths.get("assets", "config.cfg");

    private final Client net;
    private final Map<String, String> config;

    public static void main(String[] args) {
        try {
            System.setProperty("java.library.path", "lib");
            System.setProperty("org.lwjgl.librarypath", Paths.get("lib", "native").toAbsolutePath().toString());
            Map<String, String> config = loadConfig();
            AppGameContainer gc = new AppGameContainer(new ElementsClient(config));
            gc.setDisplayMode(1600, 1000, false);
            gc.setTargetFrameRate(60);
            gc.setAlwaysRender(true);
            gc.start();
        } catch (SlickException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> loadConfig() throws IOException {
        return Files.readAllLines(configFile).stream()
            .filter(line -> !line.startsWith("#"))
            .map(line -> line.split(": ?"))
            .filter(split -> split.length == 2)
            .collect(Collectors.toMap(
                split -> split[0],
                split -> split[1]
            ));
    }

    public ElementsClient(Map<String, String> config) {
        super("Elements");
        this.config = config;
        // yoda compare avoids NPE on missing key
        if ("false".equals(config.get("local"))) {
            try {
                net = new InternetClient(config.get("host"), Integer.parseInt(config.get("port")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            var thread = Executors.newSingleThreadExecutor();
            var localServer = new LocalServer();
            var server = new GameServer(localServer);
            thread.execute(server::run);
            net = localServer.createClient();
        }
    }

    @Override
    public void initStatesList(GameContainer gc) {
//        addState(new MainMenu(net));
        addState(new GameClient(net, config));
    }
}
