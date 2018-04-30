package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Graphics;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.util.Box;
import se.tdfpro.elements.util.Vec2;

import java.util.function.Supplier;

import static se.tdfpro.elements.util.Utils.clamp;

public class ProgressBar extends InterfaceComponent {

    private final Supplier<Float> progress;
    private final Vec2 size;

    public ProgressBar(Box dimension, Supplier<Float> progress) {
        super(dimension.topLeft());
        this.size = dimension.size();
        this.progress = progress;
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        var progress = clamp(this.progress.get(), 0, 1);
        g.setColor(getColor());
        g.fillRect(0, 0, size.x * progress, size.y);
        g.drawRect(0, 0, size.x, size.y);
    }
}
