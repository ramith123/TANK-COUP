import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.util.Random;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class PowerUp {
    private ImageManager imageManager;
    private Image image;
    private int x, y;

    public PowerUp(String name) {
        y = 100;
        x = getRandomX();
        imageManager = ImageManager.getInstance();
        image = imageManager.getImage(name + ".png");
    }

    private int getRandomX() {
        Random r = new Random();
        return 10 + r.nextInt(GameWindow.pWidth - 60);
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXAndY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, image.getWidth(null), image.getHeight(null));
    }

}
