package se.tdfpro.elements.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ElementsClient extends StateBasedGame {

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
    }

    @Override
    public void initStatesList(GameContainer gc) {
        addState(new MainMenu());
        addState(new GameClient());
    }
}
