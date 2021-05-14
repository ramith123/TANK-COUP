import javax.swing.*; // need this for GUI objects
import javax.swing.event.MouseInputListener;

import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.image.BufferStrategy; // need this to implement page flipping

public class GameWindow extends JFrame implements Runnable, KeyListener, MouseInputListener {
	private static final int NUM_BUFFERS = 2; // used for page flipping
	private int heightOffset = 30;
	private int widthOffset = 7;
	public static int pWidth = 1080;
	public static int pHeight = 720;
	private Thread gameThread = null; // the thread that controls the game
	private volatile boolean isRunning = false; // used to stop the game thread

	private StartWindow startWindow;
	private BufferedImage image; // drawing area for each frame

	private boolean finishedOff = false; // used when the game terminates
	private volatile boolean isPaused = false;

	private GraphicsDevice device; // used for full-screen exclusive mode
	private Graphics gScr;
	private BufferStrategy bufferStrategy;

	TerrainEntityManager terrainEntityManager;
	boolean gameStart, blurScreen, gameOver;
	private GameOver gameOverScreen;

	private GradientPaint backgroundGrad;

	public GameWindow() {

		super("TANK COUP");
		Color gradUpperColor = new Color(15, 212, 255);
		Color gradLowerColor = new Color(155, 255, 237);
		backgroundGrad = new GradientPaint(pWidth / 2, 0, gradUpperColor, pWidth / 2, pHeight / 2, gradLowerColor);
		gameStart = gameOver = false;
		blurScreen = true;

		initFullScreen();
		image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		terrainEntityManager = new TerrainEntityManager(this);
		startWindow = new StartWindow(this);
		// soundManager = SoundManager.getInstance();
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		startGame();
	}

	// implementation of Runnable interface

	public void run() {
		try {
			isRunning = true;
			while (isRunning) {
				gameUpdate();
				draw();
				Thread.sleep(42);
			}
		} catch (InterruptedException e) {
		}

		finishOff();
	}

	/*
	 * This method performs some tasks before closing the game. The call to
	 * System.exit() should not be necessary; however, it prevents hanging when the
	 * game terminates.
	 */

	public void finishOff() {
		if (!finishedOff) {
			finishedOff = true;
			restoreScreen();
			System.exit(0);
		}
	}

	/*
	 * This method switches off full screen mode. The display mode is also reset if
	 * it has been changed.
	 */

	private void restoreScreen() {
		Window w = device.getFullScreenWindow();

		if (w != null)
			w.dispose();

		device.setFullScreenWindow(null);
	}

	public void gameUpdate() {
		if (gameStart) {
			terrainEntityManager.gameUpdate();
		} else {

		}

	}

	private void draw() {

		try {
			gScr = bufferStrategy.getDrawGraphics();
			gameRender(gScr);
			gScr.dispose();
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents of buffer lost.");

			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)

			Toolkit.getDefaultToolkit().sync();
		} catch (Exception e) {
			e.printStackTrace();
			isRunning = false;
		}

	}

	public void gameRender(Graphics gScr) { // draw the game objects
		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		imageContext.setBackground(Color.WHITE);
		imageContext.clearRect(0, 0, pWidth, pHeight);
		imageContext.setPaint(backgroundGrad);
		imageContext.fill(new Rectangle.Double(0, 0, pWidth, pHeight));
		if (blurScreen) {
			terrainEntityManager.pauseGame();
			terrainEntityManager.drawStartScreen(imageContext);
			imageContext.dispose();
			image = Util.blurImage(image);
			imageContext = (Graphics2D) image.getGraphics();
		}
		if (gameStart && !blurScreen) {
			terrainEntityManager.draw(imageContext);
		}
		if (!gameStart && blurScreen) {
			startWindow.draw(imageContext);
		}
		if (gameOver && gameStart && blurScreen) {
			gameOverScreen.draw(imageContext);
		}

		Graphics2D g2 = (Graphics2D) gScr;

		g2.drawImage(image, widthOffset, heightOffset, pWidth, pHeight, null);
		imageContext.dispose();
		g2.dispose();
	}

	private void initFullScreen() { // standard procedure to get into FSEM

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		// setUndecorated(true); // no menu bar, borders, etc.
		setIgnoreRepaint(true); // turn off all paint events since doing active
		// rendering
		setResizable(false); // screen cannot be resized

		if (!device.isFullScreenSupported()) {
			System.out.println("Full-screen exclusive mode not supported");
			System.exit(0);
		}

		// device.setFullScreenWindow(this); // switch on full-screen exclusive mode

		// we can now adjust the display modes, if we wish

		// showCurrentMode();
		setLocationRelativeTo(null);

		System.out.println("Width of window is " + pWidth);
		System.out.println("Height of window is " + pHeight);
		setPreferredSize(new Dimension(pWidth, pHeight));
		pack();
		setVisible(true);
		try {
			createBufferStrategy(NUM_BUFFERS);
		} catch (Exception e) {
			System.exit(0);
		}

		bufferStrategy = getBufferStrategy();
	}

	// This method
	// provides details
	// about the
	// current display mode.

	// private void showCurrentMode() {

	// DisplayMode dm = device.getDisplayMode();

	// System.out.println("Current Display Mode: (" + dm.getWidth() + "," +
	// dm.getHeight() + "," + dm.getBitDepth()
	// + "," + dm.getRefreshRate() + ") ");
	// }

	// Specify screen areas for the buttons and create bounding rectangles

	public void startGame() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();

		}

	}

	// displays a message to the screen when the user stops the game

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

			terrainEntityManager.leftKeyPressed();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			terrainEntityManager.rightKeyPressed();
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			terrainEntityManager.upKeyPressed();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			terrainEntityManager.downKeyPressed();
		}
		if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			terrainEntityManager.pauseKeyPressed();
			blurScreen = blurScreen ? false : true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!gameStart) {
				gameStart = true;
				blurScreen = gameOver = false;

				terrainEntityManager.unPauseGame();
			} else {
				terrainEntityManager.spaceKeyPressed();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!gameStart && !gameOver)
			startWindow.mouse(e.getX(), e.getY(), true);
		if (gameOver)
			gameOverScreen.mouse(e.getX(), e.getY(), true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameStart && !gameOver)
			startWindow.mouse(e.getX(), e.getY(), false);
		if (gameOver)
			gameOverScreen.mouse(e.getX(), e.getY(), false);

	}

	public void gameOver(int win) {
		gameOverScreen = new GameOver(this, win);
		gameOver = true;
		blurScreen = true;
	}

	public void restartGame() {
		this.terrainEntityManager = new TerrainEntityManager(this);
	}

}