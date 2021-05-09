
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
        t1 = new Tank(testColor);
        t2 = new Tank(testColor);
        terrainManager = new TerrainManager();
        terrainIndex = 200;
        moveRight();
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

    public void moveRight() {
        terrainIndex++;
        int index = (t1.getSpeed() * terrainIndex) % (terrainManager.pointSize - 1);
        Point2D p = terrainManager.getPoint(index);
        t1.move(p);
        t1.rotateTank(Terrain.angleTo(p, terrainManager.getPoint(index + 1)));
        ;
        System.out.println(terrainIndex);
    }

    public void moveLeft() {
        terrainIndex--;
        if (terrainIndex < 0)
            terrainIndex = 0;
        int index = (t1.getSpeed() * terrainIndex) % (terrainManager.pointSize - 1);
        Point2D p = terrainManager.getPoint(index);
        t1.move(p);
        t1.rotateTank(Terrain.angleTo(p, terrainManager.getPoint(index + 1)));
        ;
    }
}
