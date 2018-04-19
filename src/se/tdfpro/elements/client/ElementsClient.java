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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElementsClient extends StateBasedGame {
    private static final Path configFile = Paths.get("assets", "config.cfg");

    private final Client net;
    private final Map<String, String> config;

//    static {
//        var path = Paths.get("lib", "native").toAbsolutePath();
//        Stream.of("lwjgl64", "liblwjgl64", "jinput-dx8_64", "jinput-raw_64", "libjinput64")
//            .map(System::mapLibraryName)
//            .map(path::resolve)
//            .filter(p -> p.toFile().exists())
//            .map(Path::toString)
//            .forEach(System::load);
//        System.load(System.mapLibraryName("lwjgl64"));
//    }

    public static void main(String[] args) {
        try {
            Map<String, String> config = loadConfig();
            System.out.println("Config: " + config);
            AppGameContainer gc = new AppGameContainer(new ElementsClient(config));
            gc.setDisplayMode(1600, 1000, false);
            gc.setTargetFrameRate(60);
            gc.setAlwaysRender(true);
//            System.out.println(Arrays.toString(ClassScope.getLoadedLibraries(ClassLoader.getSystemClassLoader())));
            gc.start();
        } catch (SlickException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> loadConfig() throws IOException {
        return Files.readAllLines(configFile).stream()
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
        if("false".equals(config.get("local"))) {
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
class ClassScope {
    private static final java.lang.reflect.Field LIBRARIES;
    static {
        try {
            LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            LIBRARIES.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public static String[] getLoadedLibraries(final ClassLoader loader) {

        try {
            final Set<String> libraries = (Set<String>) LIBRARIES.get(loader);
            return libraries.toArray(new String[] {});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
