
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class TerrainEntityManager {

    private Tank t1, t2;
    private Color testColor = Color.GREEN;
    private boolean testDir = false;
    private int testDeg = 0;
    private JFrame window;
    private TerrainManager terrainManager;
    private int terrainIndex;
    /*
     * private GraphicsConfiguration gc;
     * 
     * // host sprites used for cloning private Sprite playerSprite; private Sprite
     * musicSprite; private Sprite coinSprite; private Sprite goalSprite; private
     * Sprite grubSprite; private Sprite flySprite;
     */

    public TerrainEntityManager(JFrame window) {
        this.window = window;
        t1 = new Tank(100, 200, testColor);
        t2 = new Tank(700, 200, Color.RED);
        terrainManager = new TerrainManager();
        terrainIndex = 200;
        rightKeyPressed();
    }

    public void draw(Graphics2D g) {
        terrainManager.draw(g);
        t1.draw(g);
        t2.draw(g);
    }

    public void gameUpdate() {

        if (testDeg < 180 & !testDir)
            testDeg += 5;

        else {
            testDir = true;
            testDeg -= 5;
            // t1.rotateBarrel(testDeg);
        }
        if (testDeg < 0) {
            testDir = false;

        }
        t1.rotateBarrel(testDeg);

    }

    public void rightKeyPressed() {

        t1.moveRight(terrainManager);
        t2.moveRight(terrainManager);
    }

    public void leftKeyPressed() {

        t1.moveLeft(terrainManager);
        t2.moveLeft(terrainManager);
    }
}
