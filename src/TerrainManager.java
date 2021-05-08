import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class TerrainManager {
    private Terrain terrain;
    private int xSIZE, ySIZE;
    private ArrayList<Point2D> points;

    public TerrainManager() {
        terrain = new Terrain();
        xSIZE = GameWindow.pWidth;
        ySIZE = GameWindow.pHeight;
        getPointsFromTerrainShape();
    }

    public void draw(Graphics2D g) {
        Shape s = terrain.getTerrainShape();
        AffineTransform at = g.getTransform();
        at.setToTranslation(0, 0);
        g.setColor(Color.CYAN);
        g.transform(at);
        g.fill(s);

    }

    private void getPointsFromTerrainShape() {

        points = new ArrayList<Point2D>();
        Shape s = terrain.getTerrainShape();
        PathIterator p = s.getPathIterator(null, 0);

        while (!p.isDone()) {
            double[] coords = new double[6];
            int returnVal = p.currentSegment(coords);
            if (returnVal == PathIterator.SEG_LINETO || returnVal == PathIterator.SEG_MOVETO)
                points.add(new Point2D.Double(coords[0], coords[1]));

            p.next();
        }
        points.remove(new Point2D.Double(0, 0));
        points.remove(new Point2D.Double(GameWindow.pWidth, GameWindow.pHeight));
        points.remove(new Point2D.Double(0, GameWindow.pHeight));

        for (Point2D i : points) {
            System.out.println(i.toString());
        }
    }
}
