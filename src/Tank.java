import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.awt.geom.*;
import java.awt.Shape.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Tank {
    private int wheelSize = 10;
    private int wheelAmt = 3;
    private int gapSize = 5;
    private int verticalOffset = 5;
    private int xSIZE = 200;
    private int ySIZE = 100;
    private double railShortWidth, railLongWidth;
    BufferedImage image;
    Color color;

    public Tank(Color color) {
        image = new BufferedImage(xSIZE, ySIZE, BufferedImage.TYPE_INT_ARGB);
        this.color = color;
        railShortWidth = wheelSize * wheelAmt + gapSize * (wheelAmt - 1);
        railLongWidth = railShortWidth + (0.1 * railShortWidth);

    }

    public void draw(Graphics2D gameG) {
        renderTank();
        gameG.drawImage(image, 0, 0, xSIZE, ySIZE, null);

    }

    private void renderTank() {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.GREEN);
        g.drawRect(1, 1, xSIZE - 2, ySIZE - 2);
        renderWheels(g);
        renderRails(g);

    }

    private void renderWheels(Graphics2D g) {
        Area wheels = new Area();

        for (int i = 0; i < wheelAmt; i++) {
            Shape wheel = new Ellipse2D.Double((wheelSize + gapSize) * i, 0, wheelSize, wheelSize);
            wheels.add(new Area(wheel));
        }

        g.translate((xSIZE - wheelSize * wheelAmt - gapSize * (wheelAmt - 1)) / 2,
                ySIZE - wheels.getBounds().height - verticalOffset);
        g.setColor(Color.RED);
        g.fill(wheels);
        g.draw(wheels);
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
        g.setStroke(new BasicStroke(2));

        g.setTransform(transform);
        g.setColor(Color.BLACK);
        g.draw(rail);

    }

}
