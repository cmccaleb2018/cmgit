
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class game extends Canvas {

	private static final long serialVersionUID = 1L;

	private static int FRAME_WIDTH = 800;
	private static int FRAME_HEIGHT = 600;

	private boolean firePressed = true;
	private boolean rightPressed = true;
	private boolean leftPressed = true;

	private boolean keyWasPressed = false;

	private BufferStrategy strategy;

	public char keytyped;

	private boolean gamerun = true;

	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Shot> shots = new ArrayList<Shot>();

	private int fussilade = 3;
	private boolean shotsStillInAir = false;

	private int numBlocks = 10;

	private int angle;

	public game() {

		JFrame container = new JFrame("Shapes");

		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		panel.setLayout(null);

		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		panel.add(this);

		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		addKeyListener(new KeyInputHandler());

		// is mouse needed for this?

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
//				System.out.println(me);

//				System.out.println(me.getX() + " " + me.getY());
				// other MouseEvents: getButton as in:
				// if( e.getButton() == MouseEvent.BUTTON3 )
			}
		});

		requestFocus();

		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	private void TheLoop() {

		while (gamerun) {

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

			// DO ALL THIS BLOCK DRAWING IN Block instead - move all this there, and also
			// draw the strength.

			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			// draw blocks at new location

			for (int i = 0; i < blocks.size(); i++) {
				Block b = (Block) blocks.get(i);

				/*
				 * g.setColor(b.col); g.fillRect(b.x, b.y, b.height, b.width);
				 */
				b.draw(g);
			}

			Cannon cannon = new Cannon();
			cannon.draw(g);

			if (firePressed == true) {
				for (int x = 0; x <= fussilade; x++) {
					Shot s = new Shot();
					shots.add(s);

					firePressed = false;
				}
			}

			if (shotsStillInAir) {
				for (int i = 0; i < shots.size(); i++) {
					Shot y = (Shot) shots.get(i);

					y.move();
					y.draw(g);
					
					///  check for collision. for each block
					/// reduce strength of that block, change dx of that shot

				}
			}

			DevMessage(1, Integer.toString(angle));

			g.dispose();

			strategy.show();

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}

		}
	}

	public static void main(String[] args) {

		game g = new game();

		g.initGameItems();

		g.TheLoop();

	}

	private void initGameItems() {

		/*
		 * add random blocks with random strengths, labeled and w/ colors need block
		 * class w/ color, size, position, strength, extending rectangle
		 * 
		 * add cannon - its angle will change based on rt and left arrows space fires a
		 * ball
		 * 
		 * ball ricochets around blocks, subtracting one strength per hit, turn ends
		 * when ball hits the bottom again angles must be calculated
		 * 
		 * score is kept
		 * 
		 * some blocks when hit add an additional ball to the fusillade
		 * 
		 * blocks lower one row each time - when they reach the bottom, game over
		 *
		 * 
		 * 
		 */

		// NUMBLOCKS

		/*
		 * Random rand = new Random(); dx = rand.nextInt(10) + 1; if (dx < 6) { dx = -1;
		 * } else { dx = 1; } dy = rand.nextInt(10) + 1; if (dy < 6) { dy = -1; } else {
		 * dy = 1; }
		 */

		/// add random spawn into Block definition
		/// overload so that if args are given, they are used, and if not, use random

		for (int x = 1; x <= numBlocks; x++) {
			// Block b = new Block(100, 120, 120, 80, 10, Color.RED);
			Block b = new Block();
			blocks.add(b);
		}

		angle = 45;

	}

	private class Block extends Rectangle {

		private static final long serialVersionUID = 1L;

		private Color col;
		private int strength;

		private Block() {
			/* Rectangle r = new Rectangle(); */

			Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE,
					Color.GRAY, Color.MAGENTA };

			Random rand = new Random();
			int colPick = rand.nextInt(colors.length); // // c = colors[colPick];
			this.col = colors[colPick];
			int nx = rand.nextInt(5) * 100;
			int ny = rand.nextInt(8) * 100;
			this.x = nx;
			this.y = ny;
			int nw = rand.nextInt(8) * 10;
			int nh = rand.nextInt(8) * 10;
			this.width = nw;
			this.height = nh;
			int ns = rand.nextInt(100);
			this.strength = ns;
		}

		// overload in case I want to give specs
		private Block(int x, int y, int h, int w, int strength, Color col) {

			this.x = x;
			this.y = y;
			this.height = h;
			this.width = w;
			this.strength = strength;
			this.col = col;

		}

		private void draw(Graphics2D g) {
			g.setColor(this.col);
			g.fillRect(this.x, this.y, this.height, this.width);
			g.setColor(Color.black);
			g.drawString(Integer.toString(this.strength), this.x, this.y + 10);
		}

	}

	private class Cannon {

		private int length = 70;
		private int orig_x = 400;
		private int orig_y = 500;
		private Color c = Color.GREEN;

		private Cannon() {
		}

		private void draw(Graphics2D g) {

			/*
			 * x = r * cos(a) y = r * sin(a)
			 * 
			 * (r=1 in a unit circle)
			 */

			/*
			 * int x2 = (int) (length * Math.cos(angle)) + orig_x; int y2 = (int) (length *
			 * Math.sin(angle)) + orig_y;
			 */
			int x2 = (int) (length * Math.cos(angle)) + orig_x;
			int y2 = (int) (length * Math.sin(angle)) + orig_y;
			/*
			 * x2=orig_x; y2=orig_y-length;
			 */

			g.setColor(c);
			g.drawLine(orig_x, orig_y, x2, y2);
			String s = Integer.toString(x2) + ", " + Integer.toString(y2);

			g.drawString(s, x2, y2);
			DevMessage(2, s);

		}

	}

	private class Shot {
		private int dx_angle = 1;
		private int dy_angle = 3;
		private int x = 400;
		private int y = 500;
		private int dx = -1;
		private int dy = -1;
		private int shotSize = 10;

		private Shot() {
		}

		private void draw(Graphics2D g) {

			Ellipse2D ellipse = new Ellipse2D.Double(x, y, shotSize, shotSize);
			g.setColor(Color.WHITE);
			g.draw(ellipse);

		}

		private void move() {

			if (x > FRAME_WIDTH - shotSize || x < 0) {
				dx = -dx;
			}
			if (y > FRAME_HEIGHT - shotSize || y < 0) {
				dy = -dy;
			}

			x = x + (dx * dx_angle);
			y = y + (dy * dy_angle);

		}

	}

	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			keyWasPressed = true;

//			DevMessage(Integer.toString(e.getKeyCode()));

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
				angle--;

			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
				angle++;

			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (shotsStillInAir == false) {
					firePressed = true;
					shotsStillInAir = true;
				}
			}
		}

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//				firePressed = false;
			}
		}

		public void keyTyped(KeyEvent e) {

			keytyped = e.getKeyChar();

			// if we hit escape, then quit the game

			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}

	public void DevMessage(int index, String s) {

		int baseline_y = 590;
		int nextLine = 20;
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.drawString(s, 5, baseline_y - nextLine * index);

	}

}
