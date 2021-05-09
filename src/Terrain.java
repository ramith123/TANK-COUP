import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;
import java.lang.Math;

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
        int maxHeight = 500;
        int curveLength = screenWidth / numberOfPoints;
        int xs[] = { 0, curveLength, curveLength * 2, curveLength * 3, curveLength * 4 };
        int ys[] = { screenHeight / 2, screenHeight / 2, maxHeight, screenHeight / 2, screenHeight / 2 };
        double xStep = xs[1] / 2;

        path.moveTo(xs[0], ys[0]);
        for (int i = 0; i < xs.length - 1; i++)
            path.curveTo(xs[i], ys[i], xs[i], ys[i], (xs[i] + xs[i + 1]) / 2, (ys[i] + ys[i + 1]) / 2);
        // path.curveTo(xs[1], ys[1], xs[1], ys[1], (xs[1] + xs[2]) / 2, (ys[1] + ys[2])
        // / 2);
        // path.curveTo(xs[2], ys[2], xs[2], ys[2], (xs[2] + xs[3]) / 2, (ys[2] + ys[3])
        // / 2);
        // path.curveTo(xs[3], ys[3], xs[3], ys[3], (xs[3] + xs[4]) / 2, (ys[3] + ys[4])
        // / 2);
        path.curveTo(xs[4], ys[4], xs[4], ys[4], xs[4], ys[4]);
        path.lineTo(screenWidth, screenHeight);
        path.lineTo(0, screenHeight);
        path.closePath();
        terrainShape = path;
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