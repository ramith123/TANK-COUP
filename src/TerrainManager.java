import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class TerrainManager {
    private Terrain terrain;
    BufferedImage image;
    private int xSIZE, ySIZE;

    public TerrainManager() {
        terrain = new Terrain();
        xSIZE = GameWindow.pWidth;
        ySIZE = GameWindow.pHeight;

    }

    public void draw(Graphics2D g) {
        image = new BufferedImage(xSIZE, ySIZE, BufferedImage.TYPE_INT_ARGB);
        Shape s = terrain.getTerrainShape();
        AffineTransform at = g.getTransform();
        at.setToTranslation(0, 0);
        g.setColor(Color.CYAN);
        g.transform(at);
        g.fill(s);

    }
}
