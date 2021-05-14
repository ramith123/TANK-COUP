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
    public int pointSize;
    private int craterSize = 100;
    private boolean damageDone = false;

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
        g.setColor(new Color(136, 72, 141));
        g.transform(at);
        // g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,
        // BasicStroke.JOIN_ROUND));
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

        pointSize = points.size();

    }

    public Point2D getNextPoint(Point2D p) {
        if (damageDone)
            return points.get(points.indexOf(p) - 1);
        return points.get(points.indexOf(p) + 1);
    }

    public Point2D getIndex(int x) {
        Point2D p = null;
        for (int i = 0; i < points.size(); i++) {
            p = points.get(i);
            if ((int) p.getX() == x) {
                return p;
            }
        }
        return p;
    }

    public Shape getTerrainShape() {

        return terrain.getTerrainShape();
    }

    public void damageTerrain(Point2D p) {
        Shape terrainShape = terrain.getTerrainShape();
        Area area = new Area(terrainShape);
        Shape rhs = new Ellipse2D.Double(p.getX() - craterSize / 1.35, p.getY() - craterSize / 1.35, craterSize,
                craterSize);
        area.subtract(new Area(rhs));
        terrain.setTerrainShape(area);
        damageDone = true;
        getPointsFromTerrainShape();

    }

}
