import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Terrain {
    private Shape terrainShape;
    private int yOffset = 100;
    private int maxNumberOfPoints = 5;
    private int minNumberOfPoints = 1;

    public Terrain() {
        generateShape();
    }

    public void generateShape() {
        GeneralPath path = new GeneralPath();
        int screenWidth = GameWindow.pWidth;
        int screenHeight = GameWindow.pHeight;
        int numberOfPoints = 4;

        double[][] points = createPoints(numberOfPoints);

        path.moveTo(points[0][0], points[0][1]);
        double x, y, hx, hy;
        x = y = hx = hy = 0;
        for (int i = 0; i < points.length - 1; i++) {
            x = points[i][0];
            y = points[i][1];
            hx = points[i + 1][0];
            hy = points[i + 1][1];
            path.curveTo(x, y, x, y, (x + hx) / 2, (y + hy) / 2);
        }
        int lastIndex = points.length - 1;
        x = points[lastIndex][0];
        y = points[lastIndex][1];
        path.curveTo(x, y, x, y, x, y);
        path.lineTo(screenWidth, screenHeight);
        path.lineTo(0, screenHeight);
        path.closePath();
        terrainShape = path;
    }

    private double[][] createPoints(int numberOfPoints) {
        int curveLength = GameWindow.pWidth / numberOfPoints;
        int screenHeight = GameWindow.pHeight;
        double[][] arr = { { 0, 200 }, { curveLength, screenHeight / 2 }, { curveLength * 2, 500 },
                { curveLength * 3, 200 }, { curveLength * 4, 100 } };
        return arr;
        // TODO: Finish this
    }

    public Shape getTerrainShape() {
        return terrainShape;
    }

    // public void generateShape() {
    // GeneralPath path = new GeneralPath();
    // int screenWidth = GameWindow.pWidth;
    // int screenHeight = GameWindow.pHeight;
    // int numberOfPoints = (int) (Math.random() * (maxNumberOfPoints -
    // minNumberOfPoints + 1)) + minNumberOfPoints;
    // double randomFactor = Math.random();
    // int curveInitPointX = 0;
    // int curveInitPointY = (int) ((screenHeight / 2));
    // int curveLength = screenWidth / numberOfPoints;
    // path.moveTo(curveInitPointX, curveInitPointY);
    // System.out.println(numberOfPoints);
    // for (int i = 1; i <= numberOfPoints; i++) {
    // double newX = curveLength * i;
    // double newY = (curveInitPointY * randomFactor) + curveInitPointY;
    // path.curveTo(curveInitPointX, newY, newX * randomFactor, newY, newX,
    // curveInitPointY);
    // curveInitPointX = (int) newX;
    // randomFactor = Math.random();

    // }
    // path.lineTo(screenWidth, screenHeight);
    // path.lineTo(0, screenHeight);
    // path.closePath();
    // terrainShape = path;
    // }

    // public Shape getTerrainShape() {
    // return terrainShape;
    // }

    public static double angleTo(Point2D from, Point2D to) {
        double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
        return angle;
    }

}