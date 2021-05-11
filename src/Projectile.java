
import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Projectile {

    private int x, y, ix, iy;
    private int dv = 30;
    private int circleSize = 10;
    private boolean destroyed = false;

    private double angle;
    AffineTransform at;
    private Tank tank;
    private int t;
    private double g = 3;
    private Shape boundingRect;

    public Projectile(Tank tank) {

        this.tank = tank;
        this.angle = getAngle();
        setAffineTransform();
        this.ix = (int) at.getTranslateX();
        this.iy = (int) at.getTranslateY();
        boundingRect = null;
        t = 0;
    }

    private double getAngle() {
        double tankAngle = -tank.getTankAngle();
        // System.out.println(Math.toDegrees(tankAngle));
        double barrelAngle = tank.getBarrelAngle();
        // System.out.println(Math.toDegrees(barrelAngle));
        // double calcAngle = tankAngle + barrelAngle - Math.toRadians(180);
        double calcAngle = barrelAngle - tankAngle;
        // System.out.println(Math.toDegrees(calcAngle));
        return calcAngle + Math.PI;
    }

    private void setAffineTransform() {
        AffineTransform tankAt = tank.getTankTransform();
        AffineTransform barrelAt = tank.getBarrelTransform();

        tankAt.concatenate(barrelAt);
        tankAt.translate(tank.getBarrelWidth(), 0);
        at = tankAt;

    }

    public void draw(Graphics2D gameG) {
        BufferedImage projectile = new BufferedImage(circleSize, circleSize, BufferedImage.TYPE_INT_ARGB);

        Shape circle = new Ellipse2D.Double(0, 0, circleSize, circleSize);
        Graphics2D g = (Graphics2D) projectile.getGraphics();

        g.setColor(Color.BLACK);
        g.fill(circle);
        gameG.drawImage(projectile, at, null);
        // gameG.setColor(Color.RED);
        // gameG.draw(boundingRect);

    }

    public void move() {
        // Rectangle2D rect = boundingRect;
        // System.out.println(rect);
        calculateTrajectory();
        // at.translate(dx, 0);
        at.setToTranslation(x, y);
        at.rotate(angle);
        at.translate(0, -circleSize - 1);
        Shape rect = new Rectangle2D.Double(0, 1, circleSize, circleSize);
        boundingRect = at.createTransformedShape(rect);
        ;

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

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    private void calculateTrajectory() {
        // System.out.println(Math.toDegrees(angle));
        x = (int) (this.ix - (dv * t * Math.cos(angle)));
        y = (int) (this.iy - ((dv * t * Math.sin(angle)) - (0.5 * g * t * t)));
        t++;
        // x = ix;
        // y = iy;
    }

    public Shape getBoundingRect() {
        return boundingRect;
    }

}
