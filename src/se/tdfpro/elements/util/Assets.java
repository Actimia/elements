package se.tdfpro.elements.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Assets {

    void createGradientImage() {
        var size = 100;
        var max = size / 2;
        var img = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var x = i - (size / 2);
                var y = j - (size / 2);
                var dist = Math.sqrt(x * x + y * y);
                if (dist > max) dist = max;
                var q = dist/max;
                q *= q;
                var alpha = 255 - 255 * q;
                var color = (int) alpha << 24 | 0xffffff;
                img.setRGB(i, j, color);
            }
        }

        try {
            var file = new File("assets/gradient2.png");
            if (!file.exists()) file.createNewFile();
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var a = new Assets();
        a.createGradientImage();
    }
}
