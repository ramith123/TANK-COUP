import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;

import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes

public class Terrain {
    private Shape terrainShape;
    private int yOffset = 200;

    public Terrain() {
        generateShape();
    }

    public void generateShape() {
        GeneralPath path = new GeneralPath();
        int screenWidth = GameWindow.pWidth;
        int screenHeight = GameWindow.pHeight;
        int numberOfPoints = 5;

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
        int screenWidth = GameWindow.pWidth;
        int screenHeight = GameWindow.pHeight;
        int curveLength = GameWindow.pWidth / numberOfPoints;

        double[][] arr = new double[numberOfPoints + 2][2];
        for (int i = 0; i <= numberOfPoints; i++) {
            int xAxis = curveLength * i;
            int yAxis = Util.getRandomNumberUsingNextInt((screenHeight / 2) - yOffset, (screenHeight / 2) + yOffset);
            double temp[] = { xAxis, yAxis };
            arr[i] = temp;
        }
        double temp[] = { screenWidth, screenHeight / 2 };
        arr[numberOfPoints + 1] = temp;
        return arr;
    }

    public Shape getTerrainShape() {
        return terrainShape;
    }

    public void setTerrainShape(Shape s) {
        terrainShape = s;

    }
    // Old Map generation
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
        // System.out.println(angle);
        return angle;
    }

}