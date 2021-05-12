import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.util.Random;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class PowerUpManager {
    private ImageManager imageManager;
    private PowerUp activePowerUp;
    private boolean powerUpDidCollide = false;
    private Random rand;

    private int initX, initY;

    public PowerUpManager() {
        activePowerUp = null;
        rand = new Random();

    }

    public void draw(Graphics2D g) {
        if (powerUpDidCollide)
            return;
        if (isPowerUp() && !powerUpDidCollide) {
            g.drawImage(activePowerUp.getImage(), activePowerUp.getX(), activePowerUp.getY(), null);
        }
    }

    public void move() {

    }

    public void getPowerUp() {
        if (powerUpDidCollide || !isPowerUp()) {

            activePowerUp = new PowerUp("health");
            powerUpDidCollide = false;
        }

    }

    public Rectangle2D getBound() {
        return null;
    }

    public void powerUpCollision(Shape boundingRect) {
        if (activePowerUp != null && !powerUpDidCollide)
            powerUpDidCollide = boundingRect.intersects(activePowerUp.getBounds());
    }

    public boolean isPowerUp() {
        if (activePowerUp == null)
            return false;
        return true;
    }

    public void activatePowerUp(Tank currentTank, Tank hitTank) {
        if (!powerUpDidCollide)
            return;

        activePowerUp = null;

    }

}
