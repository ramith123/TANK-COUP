
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
    private Animation smallExplosion, largeExplosion;
    private ImageManager imageManager;

    /*
     * private GraphicsConfiguration gc;
     * 
     * // host sprites used for cloning private Sprite playerSprite; private Sprite
     * musicSprite; private Sprite coinSprite; private Sprite goalSprite; private
     * Sprite grubSprite; private Sprite flySprite;
     */

    public TerrainEntityManager(JFrame window) {
        imageManager = ImageManager.getInstance();
        round = 1;
        roundStatus = 0;
        timer = GUITimer.getInstance();
        // timer.pauseTimer();
        this.window = window;
        terrainManager = new TerrainManager();
        t1 = new Tank(100, 200, Color.GREEN, fuelAmt, this);
        t1.rotateBarrel(p1TankAngle);
        t2 = new Tank(GameWindow.pWidth - 100, 200, Color.RED, fuelAmt, this);
        t2.rotateBarrel(p2TankAngle);
        getRandomTank();
        gameGUI = new GameGUI(t1, t2, this, window);
        powerUpManager = new PowerUpManager(this);
        loadAnimation();
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

    void switchTanks() {
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
        smallExplosion.draw(g);
    }

    public void drawStartScreen(Graphics2D g) {
        terrainManager.draw(g);
        t1.draw(g);
        t2.draw(g);

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
        smallExplosion.update();

    }

    private void timeCheck() {
        if (timer.getTimer() <= 0) {
            nextRound();
        }
    }

    private void checkHealth() {
        if (t1.getHealth() <= 0 && t2.getHealth() <= 0) {
            ((GameWindow) window).gameOver(2);
            pauseGame();

        } else if (t1.getHealth() <= 0) {
            ((GameWindow) window).gameOver(1);
            pauseGame();
            // TODO: p2 wins
        } else if (t2.getHealth() <= 0) {
            ((GameWindow) window).gameOver(0);
            pauseGame();
            // TODO:P1 wins

        }
    }

    public void rightKeyPressed() {

        if (timer.isPaused())
            return;
        if (roundStatus == 0)
            currentTank.moveRight();
        else if (roundStatus == 1)
            currentTank.changeBarrelAngleRight();

    }

    public void leftKeyPressed() {
        if (timer.isPaused())
            return;
        if (roundStatus == 0)
            currentTank.moveLeft();
        else if (roundStatus == 1)
            currentTank.changeBarrelAngleLeft();
    }

    public void createProjectile() {
        shot = new Projectile(currentTank);
    }

    private void destroyProjectile() {
        Point2D p = null;
        if (shot != null) {
            p = shot.collisionCheck(terrainManager);
            Tank tankHit = shot.collisionCheck(otherTank);
            if (shot.isDestroyed()) {
                shot = null;
                powerUpManager.activatePowerUp(currentTank, tankHit);
                nextRound();
            }
            if (tankHit != null) {
                tankHit.decreaseHealth();
                smallExplosion.start(tankHit.getTankTransform());
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

    public int getRound() {
        return round;
    }

    public void endGame() {

    }

    private void loadAnimation() {

        Image animImage1 = imageManager.getImage("se_01.png");
        Image animImage2 = imageManager.getImage("se_02.png");
        Image animImage3 = imageManager.getImage("se_03.png");
        Image animImage4 = imageManager.getImage("se_04.png");
        Image animImage5 = imageManager.getImage("se_05.png");
        Image animImage6 = imageManager.getImage("se_06.png");
        Image animImage7 = imageManager.getImage("se_07.png");
        Image animImage8 = imageManager.getImage("se_08.png");
        Image animImage9 = imageManager.getImage("se_09.png");

        // create animation object and insert frames

        smallExplosion = new Animation(200, 200);

        smallExplosion.addFrame(animImage1, 70);
        smallExplosion.addFrame(animImage2, 70);
        smallExplosion.addFrame(animImage3, 70);
        smallExplosion.addFrame(animImage4, 70);
        smallExplosion.addFrame(animImage5, 70);
        smallExplosion.addFrame(animImage6, 70);
        smallExplosion.addFrame(animImage7, 70);
        smallExplosion.addFrame(animImage8, 70);
        smallExplosion.addFrame(animImage9, 70);

    }
}