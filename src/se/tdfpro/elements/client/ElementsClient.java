package se.tdfpro.elements.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;

public class ElementsClient extends StateBasedGame {

    private Network net;

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
            net = new Network("localhost", 7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initStatesList(GameContainer gc) {
        addState(new MainMenu(net));
        addState(new GameClient(net));
    }
}
