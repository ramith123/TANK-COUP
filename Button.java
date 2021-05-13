import javax.swing.*; // need this for GUI objects
import javax.swing.border.StrokeBorder;

import java.lang.Math;
import java.awt.geom.*;
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;

public class Button {
    GameWindow window;
    String text;
    BufferedImage buttonImageNotClicked, buttonImageClicked;
    private int buttonWidth;
    private int buttonHeight;
    private int x, y;

    public Button(GameWindow window, String text, int x, int y, int buttonWidth, int buttonHeight) {
        this.x = x;
        this.y = y;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.window = window;
        this.text = text;
        buttonImageNotClicked = new BufferedImage(buttonWidth, buttonHeight, BufferedImage.TYPE_INT_ARGB);
        buttonImageClicked = new BufferedImage(buttonWidth, buttonHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) buttonImageNotClicked.getGraphics();
        g.setColor(Color.GREEN);
        g.fill3DRect(0, 0, buttonWidth, buttonHeight, true);
        setText(g);
        g.dispose();
        g = (Graphics2D) buttonImageClicked.getGraphics();
        g.setColor(Color.GREEN);
        g.fill3DRect(0, 0, buttonWidth, buttonHeight, false);
        setText(g);

    }

    public BufferedImage getImageNotClicked() {
        return buttonImageNotClicked;
    }

    public BufferedImage getImageClicked() {
        return buttonImageClicked;
    }

    private void setText(Graphics2D g) {

        // g.setClip(0, 0, pWidth, pHeight);
        Font font = new Font("SansSerif", Font.BOLD, 24);
        FontMetrics metrics = window.getFontMetrics(font);

        String msg = text;

        int x = (buttonWidth - metrics.stringWidth(msg)) / 2;
        int y = (buttonHeight - metrics.getHeight()) / 2 + metrics.getAscent();

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, x, y);

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(this.x, this.y, buttonWidth, buttonHeight);
    }
}
