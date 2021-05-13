import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartWindow {
    private GameWindow window;
    private Button startButton, quitButton;
    private BufferedImage drawStartButton, drawQuitButton;
    private int buttonWidth = 200;
    private int buttonHeight = 100;
    private int buttonGap = 10;
    private Font customFont;

    public StartWindow(GameWindow window) {
        try {
            // create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/PressStart2P-Regular.ttf"))
                    .deriveFont(100f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        this.window = window;
        startButton = new Button(window, "Start Game", (GameWindow.pWidth - buttonWidth) / 2,
                (GameWindow.pHeight - buttonHeight) / 2, buttonWidth, buttonHeight);
        quitButton = new Button(window, "Quit Game", (GameWindow.pWidth - buttonWidth) / 2,
                (GameWindow.pHeight - buttonHeight) / 2 + buttonHeight + buttonGap, buttonWidth, buttonHeight);
        drawStartButton = startButton.getImageNotClicked();
        drawQuitButton = quitButton.getImageNotClicked();

    }

    public void draw(Graphics2D g) {
        setTitle(g);
        g.drawImage(drawStartButton, startButton.getX(), startButton.getY(), null);
        g.drawImage(drawQuitButton, quitButton.getX(), quitButton.getY(), null);
    }

    public void mouse(int x, int y, boolean click) {
        // System.out.println(x + " " + y);
        if (startButton.getBounds().contains(x, y)) {
            drawStartButton = startButton.getImageClicked();
            if (click) {
                System.out.println("test");
                window.gameStart = true;
                window.blurScreen = false;
                window.terrainEntityManager.unPauseGame();
            }
        } else {
            drawStartButton = startButton.getImageNotClicked();
        }
        if (quitButton.getBounds().contains(x, y)) {
            drawQuitButton = quitButton.getImageClicked();
            if (click) {
                window.finishOff();
            }
        } else {
            drawQuitButton = quitButton.getImageNotClicked();
        }

    }

    private void setTitle(Graphics2D g) {

        // g.setClip(0, 0, pWidth, pHeight);

        FontMetrics metrics = window.getFontMetrics(customFont);

        String msg = "TANK COUP";

        int x = (GameWindow.pWidth - metrics.stringWidth(msg)) / 2;
        int y = (metrics.getHeight() + metrics.getAscent());

        g.setColor(Color.BLACK);
        g.setFont(customFont);
        g.drawString(msg, x, y);

    }

}
