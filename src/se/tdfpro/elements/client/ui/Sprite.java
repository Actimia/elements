package se.tdfpro.elements.client.ui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import se.tdfpro.elements.client.GameClient;
import se.tdfpro.elements.server.physics.Vec2;

public class Sprite extends InterfaceComponent {
    private final Image image;
    private final Vec2 size;

    public Sprite(Vec2 pos, Vec2 size, Image img) {
        super(pos);
        this.image = img;
        this.size = size;
    }

    public Sprite(Vec2 pos, Image img) {
        this(pos, new Vec2(img.getWidth(), img.getHeight()), img);
    }

    public Sprite(Vec2 pos, String imgref) {
        this(pos, GameClient.textures.get(imgref));
    }

    @Override
    public void draw(GameClient game, Graphics g) {
        g.drawImage(image,
            0, 0, size.x, size.y,
            0, 0, image.getWidth(), image.getHeight(),
            getColor());
    }
}
