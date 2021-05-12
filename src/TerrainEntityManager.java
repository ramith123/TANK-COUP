
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JFrame;

public class TerrainEntityManager {

    private Tank t1, t2, currentTank, otherTank;
    private Projectile shot;

    private int p2TankAngle = 150;
    private int p1TankAngle = 30;
    private int fuelAmt = 25;
    private int round;

    private JFrame window;
    private TerrainManager terrainManager;
    private int roundStatus;
    private GUITimer timer;
    private GameGUI gameGUI;
    private PowerUpManager powerUpManager;

    /*
     * private GraphicsConfiguration gc;
     * 
     * // host sprites used for cloning private Sprite playerSprite; private Sprite
     * musicSprite; private Sprite coinSprite; private Sprite goalSprite; private
     * Sprite grubSprite; private Sprite flySprite;
     */

    public TerrainEntityManager(JFrame window) {
        round = 0;
        roundStatus = 0;
        timer = GUITimer.getInstance();
        timer.pauseTimer();
        this.window = window;
        terrainManager = new TerrainManager();
        t1 = new Tank(100, 200, Color.GREEN, fuelAmt, this);
        t1.rotateBarrel(p1TankAngle);
        t2 = new Tank(GameWindow.pWidth - 100, 200, Color.RED, fuelAmt, this);
        t2.rotateBarrel(p2TankAngle);
        getRandomTank();
        gameGUI = new GameGUI(t1, t2, this, window);
        powerUpManager = new PowerUpManager();
    }

    private void getRandomTank() {
        Random r = new Random();
        int i = r.nextInt(2);
        if (i == 1) {
            currentTank = t1;
            otherTank = t2;
        } else {
            currentTank = t2;
            otherTank = t1;
        }

    }

    private void switchTanks() {
        Tank temp = currentTank;
        currentTank = otherTank;
        otherTank = temp;
    }

    private void nextRound() {
        powerUpManager.getPowerUp();
        currentTank.resetFuel();
        switchTanks();
        roundStatus = 0;
        round++;
        timer.resetTimer();
    }

    private void nextStatus() {
        roundStatus++;
    }

    public void draw(Graphics2D g) {
        terrainManager.draw(g);
        t1.draw(g);
        t2.draw(g);
        if (shot != null)
            shot.draw(g);
        gameGUI.draw(g);
        powerUpManager.draw(g);
    }

    public void gameUpdate() {
        powerUpManager.move();

        if (shot != null) {
            shot.move();
            powerUpManager.powerUpCollision(shot.getBoundingRect());

        }
        destroyProjectile();
        checkHealth();
        // decreaseTime();
        timer.getTimer();
        timeCheck();
        gameGUI.gameUpdate();

    }

    private void timeCheck() {
        if (timer.getTimer() <= 0) {
            nextRound();
        }
    }

    private void checkHealth() {
        if (t1.getHealth() <= 0 && t2.getHealth() <= 0) {
            // TODO: draw

        } else if (t1.getHealth() <= 0) {
            // TODO: p2 wins
        } else if (t2.getHealth() <= 0) {
            // TODO:P1 wins

        }
    }

    public void rightKeyPressed() {

        if (timer.isPaused())
            return;
        if (roundStatus == 0)
            currentTank.moveRight();
        else if (roundStatus == 1)
            currentTank.changeBarrelAngle(-8);

    }

    public void leftKeyPressed() {
        if (timer.isPaused())
            return;
        if (roundStatus == 0)
            currentTank.moveLeft();
        else if (roundStatus == 1)
            currentTank.changeBarrelAngle(8);
    }

    public void createProjectile() {
        shot = new Projectile(currentTank);
    }

    private void destroyProjectile() {
        Point2D p = null;
        if (shot != null) {
            p = shot.collisionCheck(terrainManager);
            Tank tankHit = shot.collisionCheck(otherTank);
            powerUpManager.activatePowerUp(currentTank, tankHit);
            if (shot.isDestroyed()) {
                shot = null;
                nextRound();
            }
            if (tankHit != null) {
                tankHit.decreaseHealth();
            }
            if (p != null)
                terrainManager.damageTerrain(p);
        }

    }

    public void upKeyPressed() {
        if (timer.isPaused())
            return;
        if (roundStatus == 1)
            currentTank.increasePower(5);
    }

    public void downKeyPressed() {
        if (timer.isPaused())
            return;
        if (roundStatus == 1)
            currentTank.decreasePower(5);

    }

    public void spaceKeyPressed() {
        if (timer.isPaused())
            return;
        nextStatus();
        if (roundStatus == 2) {
            createProjectile();
        }
    }

    public TerrainManager getTerrainManager() {
        return terrainManager;
    }

    public void pauseKeyPressed() {
        if (timer.isPaused())
            timer.resumeTimer();
        else
            timer.pauseTimer();
    }

    public void unPauseGame() {
        if (timer.isPaused())
            timer.resumeTimer();

    }

    public void pauseGame() {
        timer.pauseTimer();
    }

    public Tank getCurrentTank() {
        return currentTank;
    }

}