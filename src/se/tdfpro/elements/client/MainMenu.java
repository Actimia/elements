package se.tdfpro.elements.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import se.tdfpro.elements.command.client.Handshake;
import se.tdfpro.elements.net.Client;

import java.util.regex.Pattern;

public class MainMenu extends BasicGameState {
    public static final int ID = 0;

    private final Client net;
    private StateBasedGame game;

    private int caretBlinkStateCounter = 0;
    private boolean caretVisible = false;

    // TODO: Viewport related stuff should be kept somewhere else
    private static final int HORIZONTAL_CENTER = 800;
    private static final int VERTICAL_CENTER = 500;

    private static final Pattern acceptedCharacters = Pattern.compile("\\w");
    private String usernameText = "";

    public MainMenu(Client net) {
        this.net = net;
    }

    private void handshake() {
        var hs = new Handshake();
        hs.username = usernameText;
        net.send(hs);

        game.enterState(1);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) {
        this.game = game;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) {
        int textWidth = g.getFont().getWidth(usernameText);
        int textHeight = g.getFont().getHeight(usernameText);
        g.drawString(
            usernameText,
            HORIZONTAL_CENTER - textWidth / 2,
            VERTICAL_CENTER - textHeight / 2
        );

        if (caretVisible) {
            g.fillRect(
                HORIZONTAL_CENTER + textWidth / 2,
                VERTICAL_CENTER - 15,
                4,
                30
            );
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) {
        caretBlinkStateCounter = (caretBlinkStateCounter + delta) % 1000;
        caretVisible = caretBlinkStateCounter < 500;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ENTER) {
            if (usernameText.length() > 2) {
                handshake();
            }
        } else if (key == Input.KEY_BACK && usernameText.length() > 0) {
            usernameText = usernameText.substring(0, usernameText.length() - 1);
        } else if (usernameText.length() < 16 && acceptedCharacters.matcher(String.valueOf(c)).matches()) {
            usernameText += c;
        }
    }

    @Override
    public int getID() {
        return ID;
    }
}
