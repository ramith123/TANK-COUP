import javax.swing.*; // need this for GUI objects
import java.awt.*; // need this for certain AWT classes
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.image.BufferStrategy; // need this to implement page flipping

public class GameWindow extends JFrame implements Runnable, KeyListener {
	private static final int NUM_BUFFERS = 2; // used for page flipping
	private int heightOffset = 30;
	private int widthOffset = 7;
	public static int pWidth = 1080;
	public static int pHeight = 720;

	private Thread gameThread = null; // the thread that controls the game
	private volatile boolean isRunning = false; // used to stop the game thread

	private BufferedImage image; // drawing area for each frame

	private boolean finishedOff = false; // used when the game terminates
	private volatile boolean isPaused = false;

	private GraphicsDevice device; // used for full-screen exclusive mode
	private Graphics gScr;
	private BufferStrategy bufferStrategy;

	private TerrainEntityManager terrainEntityManager;

	public GameWindow() {
		super("TANK COUP");

		initFullScreen();
		image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		// soundManager = SoundManager.getInstance();
		addKeyListener(this);
		startGame();
	}

	// implementation of Runnable interface

	public void run() {
		try {
			isRunning = true;
			while (isRunning) {
				if (isPaused == false) {
					gameUpdate();
				}
				draw();
				Thread.sleep(30);
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

	private void finishOff() {
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
		if (!isPaused) {
			terrainEntityManager.gameUpdate();

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

		imageContext.fillRect(0, 0, pWidth, pHeight);
		terrainEntityManager.draw(imageContext);
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

	private void startGame() {
		if (gameThread == null) {
			terrainEntityManager = new TerrainEntityManager(this);
			gameThread = new Thread(this);
			gameThread.start();

		}
	}

	// displays a message to the screen when the user stops the game

	private void gameOverMessage(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		// g.setClip(0, 0, pWidth, pHeight);
		Font font = new Font("SansSerif", Font.BOLD, 24);
		FontMetrics metrics = this.getFontMetrics(font);

		String msg = "Game Over. Thanks for playing!";

		int x = (pWidth - metrics.stringWidth(msg)) / 2;
		int y = (pHeight - metrics.getHeight()) / 2;

		g.setColor(Color.BLUE);
		g.setFont(font);
		g.drawString(msg, x, y);

	}

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

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			terrainEntityManager.spaceKeyPressed();
		}
	}

	// implementation of methods in KeyListener interface

	/*
	 * This method handles mouse clicks on one of the buttons (Pause, Stop, Start
	 * Anim, Pause Anim, and Quit).
	 */

}