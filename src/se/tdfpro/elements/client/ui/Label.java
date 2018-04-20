package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.physics.Vec2;

import java.util.function.Supplier;

public class Label extends InterfaceComponent {

    private Supplier<String> text;
    private boolean centerHorizontal = false;
    private boolean centerVertical = false;

    public Label(Vec2 position, Supplier<String> text) {
        super(position);
        this.text = text;
    }

    public Label(Vec2 position, String text) {
        this(position, () -> text);
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        var font = g.getFont();
        var text = this.text.get();
        var xOffset = centerHorizontal ? -font.getWidth(text) / 2 : 0;
        var yOffset = centerVertical ? -font.getHeight(text) / 2 : 0;

        g.drawString(text, xOffset, yOffset);
    }

    public boolean isCenteredHorizontal() {
        return centerHorizontal;
    }

    public InterfaceComponent setCenteredHorizontal(boolean centerHorizontal) {
        this.centerHorizontal = centerHorizontal;
        return this;
    }

    public boolean isCenteredVertical() {
        return centerVertical;
    }

    public InterfaceComponent setCenteredVertical(boolean centerVertical) {
        this.centerVertical = centerVertical;
        return this;
    }
}
