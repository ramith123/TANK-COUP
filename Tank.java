// import javax.swing.*; // need this for GUI objects
// import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Tank {
    private int wheelSize = 20;
    private int wheelAmt = 3;
    private int gapSize = 10;
    private int verticalOffset = 1;
    private int x, y;
    public int xSIZE = 200;
    public int ySIZE = 150;
    private int railStrokeSize = 3;
    private int bodyStrokeSize = 1;
    private int barHeight = 10;
    private int bodyHeight = 30;
    private double trapRatio = 0.2;
    private int xBound, yBound;
    private Shape boundingRect;
    private double tankRotation, barrelRotation;
    private int dx = 5;
    // private boolean tanksPlaced = false;
    private int angleChange = 4;

    private double railShortWidth, railLongWidth;
    BufferedImage image;
    private AffineTransform tankTransform;
    Color color, colorLight, colorLighter, colorBorder;
    private AffineTransform barrelTransform;

    private double health = 100;
    private double fuel, maxFuelAmount;
    private int power;
    public int powerUpperLimit = 80;
    public int powerLowerLimit = 20;
    private TerrainManager terrainManager;

    public Tank(int x, int y, Color color, int fuelAmount, TerrainEntityManager terrainEntityManager) {
        terrainManager = terrainEntityManager.getTerrainManager();
        power = powerLowerLimit;
        fuel = maxFuelAmount = fuelAmount;
        this.x = x;
        this.y = y;
        this.color = getDarkerColor(color, 0.3);
        colorBorder = color.darker();
        colorLight = getLighterColor(color, 0.4);
        colorLighter = getLighterColor(color, 0.7);
        railShortWidth = (wheelSize * wheelAmt + gapSize * (wheelAmt - 1)) * 1.1;
        railLongWidth = railShortWidth + (trapRatio * railShortWidth);
        xSIZE = (int) (railLongWidth + (railStrokeSize * 2));
        xBound = xSIZE;
        yBound = (int) (ySIZE - railShortWidth / 4) - verticalOffset - wheelSize - railStrokeSize / 2 - barHeight
                - bodyHeight;
        // image = new BufferedImage(xSIZE, ySIZE, BufferedImage.TYPE_INT_ARGB);
        moveRight();
    }

    public void draw(Graphics2D gameG) {
        image = new BufferedImage(xSIZE, ySIZE, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = renderTank();
        int newX = x - xSIZE / 2;
        int newY = y - ySIZE;

        at.setToTranslation(newX, newY);
        at.rotate(tankRotation, xSIZE / 2, ySIZE);
        tankTransform = at;
        gameG.setColor(Color.RED);
        boundingRect = getBoundingRect(newX, newY);
        // gameG.draw(boundingRect);
        gameG.drawImage(image, tankTransform, null);

    }

    public void move(Point2D point) {
        // Rectangle2D rect = boundingRect;
        // System.out.println(rect);
        this.x = (int) point.getX();
        this.y = (int) point.getY();

    }

    private AffineTransform renderTank() {
        Graphics2D g = (Graphics2D) image.getGraphics();

        renderWheels(g);
        renderRails(g);
        renderBar(g);
        renderBarrel(g);
        renderDome(g);
        renderBody(g);
        AffineTransform at = g.getTransform();
        return at;
    }

    private void renderBarrel(Graphics2D g) {

        Shape barrel = new Rectangle2D.Double(0, 0, getBarrelWidth(), barHeight);
        AffineTransform transform = g.getTransform();
        transform.setToTranslation((xSIZE / 2), (ySIZE - barrel.getBounds().height) - verticalOffset - wheelSize
                - railStrokeSize / 2 - barHeight - bodyHeight);
        transform.quadrantRotate(0, 0, barHeight / 2);
        transform.rotate(barrelRotation, 0, barHeight / 2);
        barrelTransform = transform;
        g.setTransform(transform);
        g.setColor(color);
        g.fill(barrel);
        g.setColor(colorBorder);
        g.setStroke(new BasicStroke(bodyStrokeSize));
        g.draw(barrel);
    }

    private void renderDome(Graphics2D g) {
        Shape circle = new Ellipse2D.Double(0, 0, railShortWidth / 2, railShortWidth / 2);

        AffineTransform transform = g.getTransform();
        transform.setToTranslation((xSIZE - circle.getBounds().width) / 2, (ySIZE - circle.getBounds().height / 2)
                - verticalOffset - wheelSize - railStrokeSize / 2 - barHeight - bodyHeight);
        g.setTransform(transform);

        g.setColor(colorLighter);
        g.fill(circle);
        g.setColor(colorBorder);
        g.setStroke(new BasicStroke(bodyStrokeSize));
        g.draw(circle);
    }

    private void renderBody(Graphics2D g) {

        GeneralPath body = new GeneralPath();
        body.moveTo(railLongWidth - railShortWidth, 0);
        body.lineTo(railShortWidth, 0);
        body.lineTo(railLongWidth, bodyHeight);
        body.lineTo(0, bodyHeight);
        body.closePath();
        AffineTransform transform = g.getTransform();
        transform.setToTranslation((xSIZE - body.getBounds().width) / 2,
                (ySIZE - body.getBounds().height) - verticalOffset - wheelSize - railStrokeSize / 2 - barHeight);

        g.setTransform(transform);
        g.setColor(colorLighter);
        g.fill(body);
        g.setColor(colorBorder);
        g.setStroke(new BasicStroke(bodyStrokeSize));
        g.draw(body);
    }

    private void renderBar(Graphics2D g) {
        Shape rect = new Rectangle2D.Double(0, 0, railLongWidth + (railStrokeSize * 2), barHeight);
        AffineTransform transform = g.getTransform();
        transform.setToTranslation((xSIZE - rect.getBounds().width) / 2,
                (ySIZE - rect.getBounds().height) - verticalOffset - wheelSize - railStrokeSize / 2);
        g.setTransform(transform);
        g.setColor(color);
        g.fill(rect);
    }

    private void renderRails(Graphics2D g) {
        int railHeight = wheelSize;
        GeneralPath rail = new GeneralPath();
        rail.moveTo(0, 0);
        rail.lineTo(railLongWidth, 0);
        rail.lineTo(railShortWidth, railHeight);
        rail.lineTo(railLongWidth - railShortWidth, railHeight);
        rail.closePath();

        AffineTransform transform = g.getTransform();
        transform.setToTranslation((xSIZE - rail.getBounds().width) / 2,
                (ySIZE - rail.getBounds().height) - verticalOffset);
        g.setStroke(new BasicStroke(railStrokeSize));

        g.setTransform(transform);
        g.setColor(Color.BLACK);
        g.draw(rail);

    }

    private void renderWheels(Graphics2D g) {
        Area wheels = new Area();

        for (int i = 0; i < wheelAmt; i++) {
            Shape wheel = new Ellipse2D.Double((wheelSize + gapSize) * i, 0, wheelSize, wheelSize);
            wheels.add(new Area(wheel));
        }

        g.translate((xSIZE - wheelSize * wheelAmt - gapSize * (wheelAmt - 1)) / 2,
                ySIZE - wheels.getBounds().height - verticalOffset);
        g.setColor(color);
        g.fill(wheels);
        g.draw(wheels);
    }

    private Color getLighterColor(Color color, double saturation) {
        float[] c = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        c[1] -= saturation; // saturation
        return (Color.getHSBColor(c[0], c[1], c[2]));
    }

    private Color getDarkerColor(Color color, double hue) {
        float[] c = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        c[2] -= hue; // saturation
        return (Color.getHSBColor(c[0], c[1], c[2]));
    }

    // private int getYsize() {
    // int[] pixels = image.getRGB(0, 0, xSIZE, ySIZE, null, 0, xSIZE);
    // for (int i = 0; i < pixels.length; i++) {
    // if (pixels[i] != 0) {
    // return i / xSIZE;
    // }
    // }
    // return -1;

    // }

    public void rotateTank(double degree) {

        tankRotation = degree;
    }

    private Shape getBoundingRect(int x, int y) {
        AffineTransform at = new AffineTransform();
        at.rotate(tankRotation, x + xBound / 2, y + ySIZE);
        Rectangle2D s = new Rectangle(x, y + yBound, xBound, ySIZE - yBound);

        return at.createTransformedShape(s);
    }

    public Shape getBoundingRect() {
        return boundingRect;
    }

    public void rotateBarrel(double degree) {
        if (degree > 180 && degree < 270)
            degree = 180;
        if ((degree > 270 && degree < 360) || degree < 0)
            degree = 0;
        barrelRotation = Math.toRadians(-degree);
    }

    public void changeBarrelAngleRight() {
        rotateBarrel(-Math.toDegrees(barrelRotation) - angleChange);
    }

    public void changeBarrelAngleLeft() {
        rotateBarrel(-Math.toDegrees(barrelRotation) + angleChange);
    }

    public int getSpeed() {
        return dx;
    }

    public void moveRight() {
        // if (!tanksPlaced) {
        // Point2D p = terrainManager.getIndex(x);
        // move(p);
        // rotateTank(Terrain.angleTo(p, terrainManager.getNextPoint(p)));
        // tanksPlaced = true;
        // return;
        // }
        decreaseFuel();
        if ((boundingRect != null && boundingRect.getBounds2D().getMaxX() >= GameWindow.pWidth) || getFuel() <= 0)
            return;

        Point2D p = terrainManager.getIndex(getSpeed() + x);
        move(p);
        rotateTank(Terrain.angleTo(p, terrainManager.getNextPoint(p)));
        // System.out.println(terrainIndex);
    }

    public void moveLeft() {

        decreaseFuel();
        if (boundingRect != null && boundingRect.getBounds2D().getMinX() <= 0 || getFuel() <= 0)
            return;

        Point2D p = terrainManager.getIndex(x - getSpeed());
        move(p);
        rotateTank(Terrain.angleTo(p, terrainManager.getNextPoint(p)));
    }

    public double getBarrelAngle() {
        return barrelRotation;
    }

    public Point2D getTankPosition() {
        return new Point2D.Double(this.x, this.y);
    }

    public double getTankAngle() {
        return tankRotation;
    }

    public AffineTransform getBarrelTransform() {

        return barrelTransform;
    }

    public AffineTransform getTankTransform() {

        return tankTransform;
    }

    public double getBarrelWidth() {
        return railShortWidth / 2;
    }

    public double getHealth() {
        return this.health;
    }

    public double getFuel() {
        return this.fuel;
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    public void increasePower(int by) {
        power += by;
        if (power > powerUpperLimit)
            power = powerUpperLimit;
    }

    public void decreasePower(int by) {
        power -= by;
        if (power < powerLowerLimit)
            power = powerLowerLimit;
    }

    public int getPower() {
        return power;
    }

    public void decreaseHealth() {
        health -= 20;
        if (health <= 0) {
            health = 0;
        }
        printHealth();
    }

    public void restoreHealth() {
        health += 60;
        if (health > 100)
            health = 100;
    }

    public void instantDeath() {
        health = 0;
    }

    public void printHealth() {

        System.out.println("Health of " + color + " " + health);

    }

    public void decreaseFuel() {
        fuel--;
    }

    public void increaseFuel() {
        fuel += maxFuelAmount * 0.5;
    }

    public void resetFuel() {
        fuel = maxFuelAmount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
