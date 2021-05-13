import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOver {
    private GameWindow window;
    private Button resButton, menuButton;
    private BufferedImage drawStartButton, drawQuitButton;
    private int buttonWidth = 200;
    private int buttonHeight = 100;
    private int buttonGap = 10;
    private Font customFont;
    private int win;

    public GameOver(GameWindow window, int win) {
        this.win = win;
        try {
            // create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/PressStart2P-Regular.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        this.window = window;
        resButton = new Button(window, "Retry Game", (GameWindow.pWidth - buttonWidth) / 2,
                (GameWindow.pHeight - buttonHeight) / 2, buttonWidth, buttonHeight);
        menuButton = new Button(window, "Back to Menu", (GameWindow.pWidth - buttonWidth) / 2,
                (GameWindow.pHeight - buttonHeight) / 2 + buttonHeight + buttonGap, buttonWidth, buttonHeight);
        drawStartButton = resButton.getImageNotClicked();
        drawQuitButton = menuButton.getImageNotClicked();

    }

    public void draw(Graphics2D g) {
        setTitle(g);
        g.drawImage(drawStartButton, resButton.getX(), resButton.getY(), null);
        g.drawImage(drawQuitButton, menuButton.getX(), menuButton.getY(), null);
    }

    public void mouse(int x, int y, boolean click) {
        // System.out.println(x + " " + y);
        if (resButton.getBounds().contains(x, y)) {
            drawStartButton = resButton.getImageClicked();
            if (click) {

                window.gameStart = true;
                window.blurScreen = false;
                window.gameOver = false;
                window.terrainEntityManager.unPauseGame();
                window.restartGame();
            }
        } else {
            drawStartButton = resButton.getImageNotClicked();
        }
        if (menuButton.getBounds().contains(x, y)) {
            drawQuitButton = menuButton.getImageClicked();
            if (click) {
                window.gameStart = false;
                window.blurScreen = true;
                window.gameOver = false;
                window.restartGame();
            }
        } else {
            drawQuitButton = menuButton.getImageNotClicked();
        }

    }

    private void setTitle(Graphics2D g) {

        // g.setClip(0, 0, pWidth, pHeight);

        FontMetrics metrics = window.getFontMetrics(customFont);

        String msg = "GAME OVER!\n";
        String msg2 = "";
        if (win == 0)
            msg2 = "GREEN PLAYER WINS!";
        if (win == 1)
            msg2 = "RED PLAYER WINS!";
        if (win == 2)
            msg2 = "GAME TIED";

        int m1x = (GameWindow.pWidth - metrics.stringWidth(msg)) / 2;
        int m1y = (metrics.getHeight() + metrics.getAscent());

        int m2x = (GameWindow.pWidth - metrics.stringWidth(msg2)) / 2;
        int m2y = m1y + metrics.getHeight() + metrics.getAscent();

        g.setColor(Color.BLACK);
        g.setFont(customFont);
        g.drawString(msg, m1x, m1y);
        g.drawString(msg2, m2x, m2y);

    }

}
