import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Terrain {
    private Shape terrainShape;

    public Terrain() {

        generateShape();
    }

    public void generateShape() {
        GeneralPath path = new GeneralPath();
        int screenWidth = GameWindow.pWidth;
        int screenHeight = GameWindow.pHeight;
        path.moveTo(0, screenHeight / 2);
        path.curveTo(0, 0, screenWidth / 2, 0, screenWidth / 2, screenHeight / 2);
        path.curveTo(screenWidth, screenHeight / 2, screenWidth, screenHeight / 2, screenWidth, screenHeight / 2);

        path.lineTo(screenWidth, screenHeight);
        path.lineTo(0, screenHeight);
        path.closePath();
        terrainShape = path;

    }

    public Shape getTerrainShape() {
        return terrainShape;
    }
}