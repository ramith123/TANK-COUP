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
    private boolean playSound = false;
    private Random rand;
    private TerrainEntityManager terrainEntityManager;
    private SoundManager soundManager;

    private int initX, initY;

    public PowerUpManager(TerrainEntityManager terrainEntityManager) {
        soundManager = SoundManager.getInstance();
        this.terrainEntityManager = terrainEntityManager;
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
        if (powerUpDidCollide || !isPowerUp() || terrainEntityManager.getRound() % 4 == 0) {

            activePowerUp = getRequestedPowerUp();
            powerUpDidCollide = false;
        }

    }

    public Rectangle2D getBound() {
        return null;
    }

    public void powerUpCollision(Shape boundingRect) {
        if (isPowerUp() && !powerUpDidCollide) {
            powerUpDidCollide = boundingRect.intersects(activePowerUp.getBounds());
            if (powerUpDidCollide && !playSound)
                soundManager.playSound("power.wav", false);

        }
    }

    public boolean isPowerUp() {
        if (activePowerUp == null)
            return false;
        return true;
    }

    public void activatePowerUp(Tank currentTank, Tank hitTank) {
        if (!powerUpDidCollide)
            return;

        if (isPowerUp()) {
            if (activePowerUp.toString().equals("health")) {
                currentTank.restoreHealth();
            } else if (activePowerUp.toString().equals("turn")) {
                terrainEntityManager.switchTanks();
            } else if (activePowerUp.toString().equals("damage") && hitTank != null) {
                System.out.println("here");
                hitTank.decreaseHealth();
            } else if (activePowerUp.toString().equals("death") && hitTank != null) {
                hitTank.instantDeath();

            }
        }

        activePowerUp = null;

    }

    private PowerUp getRequestedPowerUp() {
        int n = rand.nextInt(100);

        if (n > 80)
            return new PowerUp("health");
        else if (n > 50)
            return new PowerUp("turn");
        else if (n > 40)
            return new PowerUp("damage");
        else if (n > 38)
            return new PowerUp("death");

        return null;
    }

}
