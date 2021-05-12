import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class GameGUI {
    private int healthBarXSize = 200;
    private int healthBarYSize = 30;
    private JFrame window;

    private int powerBarXSize = 20;
    private int powerBarYSize = 80;
    private double t1Health, t2Health;
    private double t1Fuel, t2Fuel, maxFuel, t1Power, t2Power, maxPower;
    private Tank t1, t2, currentTank;
    private TerrainEntityManager terrainEntityManager;
    private Point2D healthBar1Location = new Point2D.Double(10, 10);
    private Point2D healthBar2Location = new Point2D.Double(GameWindow.pWidth - healthBarXSize - 25, 10);

    private Point2D powerBar1Location = new Point2D.Double(10, GameWindow.pHeight - powerBarYSize - 50);
    private Point2D powerBar2Location = new Point2D.Double(GameWindow.pWidth - powerBarXSize - 25,
            GameWindow.pHeight - powerBarYSize - 50);

    private int fuelCircleSize = 50;
    private Point2D fuel1Location = new Point2D.Double(50, GameWindow.pHeight - fuelCircleSize - 50);
    private Point2D fuel2Location = new Point2D.Double(GameWindow.pWidth - fuelCircleSize - 60,
            GameWindow.pHeight - fuelCircleSize - 50);
    private GUITimer timer;

    public GameGUI(Tank t1, Tank t2, TerrainEntityManager terrainEntityManager, JFrame window) {
        this.window = window;
        this.t1 = t1;
        this.t2 = t2;
        t1Health = t2Health = t1.getHealth();
        t1Fuel = t2Fuel = maxFuel = t1.getFuel();
        t1Power = t2Power = t1.getPower();
        powerBarYSize = t1.powerUpperLimit;
        timer = GUITimer.getInstance();
        this.terrainEntityManager = terrainEntityManager;
        currentTank = this.terrainEntityManager.getCurrentTank();

    }

    public void draw(Graphics2D g) {
        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics2D g) {
        Shape rectangle1 = new RoundRectangle2D.Double(healthBar1Location.getX(), healthBar1Location.getY(),
                healthBarXSize, healthBarYSize, 40, 40);
        Shape rectangle2 = new RoundRectangle2D.Double(healthBar2Location.getX(), healthBar2Location.getY(),
                healthBarXSize, healthBarYSize, 40, 40);
        Shape rectangle1Fill = new RoundRectangle2D.Double(healthBar1Location.getX(), healthBar1Location.getY(),
                t1Health * 2, healthBarYSize, 40, 40);
        Shape rectangle2Fill = new RoundRectangle2D.Double(healthBar2Location.getX() + healthBarXSize - (t2Health * 2),
                healthBar2Location.getY(), t2Health * 2, healthBarYSize, 40, 40);

        Shape rectangle3 = new RoundRectangle2D.Double(powerBar1Location.getX(), powerBar1Location.getY(),
                powerBarXSize, powerBarYSize, 20, 20);
        Shape rectangle4 = new RoundRectangle2D.Double(powerBar2Location.getX(), powerBar2Location.getY(),
                powerBarXSize, powerBarYSize, 20, 20);
        Shape rectangle3Fill = new RoundRectangle2D.Double(powerBar1Location.getX(),
                powerBar1Location.getY() + powerBarYSize - (t1Power - 1), powerBarXSize, t1Power, 20, 20);
        Shape rectangle4Fill = new RoundRectangle2D.Double(powerBar2Location.getX(),
                powerBar2Location.getY() + powerBarYSize - (t2Power - 1), powerBarXSize, t2Power, 20, 20);

        Shape fuelCircle1 = new Ellipse2D.Double(fuel1Location.getX(), fuel1Location.getY(), fuelCircleSize,
                fuelCircleSize);
        Shape fuelCircle2 = new Ellipse2D.Double(fuel2Location.getX(), fuel2Location.getY(), fuelCircleSize,
                fuelCircleSize);
        Area t1Fuel = get1Fuel(fuelCircle1);
        Area t2Fuel = get2Fuel(fuelCircle2);

        g.setColor(Color.WHITE);
        g.fill(fuelCircle1);
        g.fill(fuelCircle2);
        g.fill(rectangle1);
        g.fill(rectangle2);
        g.fill(rectangle3);
        g.fill(rectangle4);

        g.setColor(t1.color);
        g.fill(t1Fuel);
        g.fill(rectangle1Fill);
        g.fill(rectangle3Fill);
        g.setColor(t2.color);
        g.fill(t2Fuel);
        g.fill(rectangle2Fill);
        g.fill(rectangle4Fill);

        g.setColor(Color.BLACK);
        g.draw(rectangle1);
        g.draw(rectangle2);
        g.draw(rectangle3);
        g.draw(rectangle4);
        g.draw(fuelCircle1);
        g.draw(fuelCircle2);
        displayTimer(g);

    }

    public void gameUpdate() {
        t1Health = t1.getHealth();
        t2Health = t2.getHealth();
        t1Fuel = t1.getFuel();
        t2Fuel = t2.getFuel();
        t1Power = t1.getPower();
        t2Power = t2.getPower();
        currentTank = terrainEntityManager.getCurrentTank();
    }

    public Area get1Fuel(Shape circle) {
        Area a1 = new Area(new Rectangle2D.Double(circle.getBounds2D().getX(), circle.getBounds2D().getY() + 2,
                fuelCircleSize, (maxFuel - t1Fuel) * (fuelCircleSize / maxFuel)));
        Area b1 = new Area(circle);
        b1.subtract(a1);
        return b1;

    }

    public Area get2Fuel(Shape circle) {
        Area a1 = new Area(new Rectangle2D.Double(circle.getBounds2D().getX(), circle.getBounds2D().getY() + 2,
                fuelCircleSize, (maxFuel - t2Fuel) * (fuelCircleSize / maxFuel)));
        Area b1 = new Area(circle);
        b1.subtract(a1);
        return b1;

    }

    private void displayTimer(Graphics2D g) {

        // g.setClip(0, 0, pWidth, pHeight);
        Font font = new Font("SansSerif", Font.BOLD, 24);
        FontMetrics metrics = window.getFontMetrics(font);

        String msg = String.valueOf(timer.getTimer());

        int x = (GameWindow.pWidth - metrics.stringWidth(msg)) / 2;
        int y = 10 + (metrics.getHeight()) / 2;

        g.setColor(currentTank.color);
        g.setFont(font);
        g.drawString(msg, x, y);

    }
}
