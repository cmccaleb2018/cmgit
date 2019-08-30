
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
import java.awt.geom.Line2D;
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
	private ArrayList<Shot> deadshots = new ArrayList<Shot>();

	private int fussilade = 1;
	private boolean shotsStillInAir = false;

	// private int numBlocks = 40;

	private int angle;
	
	private Color strength_col = Color.black;
	private int cannon_rotate_rate=10;
	
	private int fire_arc;

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

			ArrayList<Block> zeroblocks = new ArrayList<Block>();

			// draw blocks at new location

			for (int i = 0; i < blocks.size(); i++) {
				Block b = (Block) blocks.get(i);

				/*
				 * g.setColor(b.col); g.fillRect(b.x, b.y, b.height, b.width);
				 */

				if (b.strength > 0) {
					b.draw(g);
				} else {
					zeroblocks.add(b);
				}
			}

			blocks.removeAll(zeroblocks);
			zeroblocks.clear();

			Cannon cannon = new Cannon();
			cannon.draw(g);

			if (firePressed == true) {
				for (int x = 0; x < fussilade; x++) {
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

					/// check for collision. for each block
					/// reduce strength of that block, change dx of that shot
				}
			}

			/// check for shots that landed at the bottom
			/// if all shots in the fussilade are dead, reset shotsStillInAir

			for (int i = 0; i < shots.size(); i++) {
				Shot y = (Shot) shots.get(i);
				if (y.alive == false) {
					deadshots.add(y);
				}

			}
			shots.removeAll(deadshots);
			deadshots.clear();

			if (shots.size() < 1) {
				shotsStillInAir = false;
			}

			for (int x = 0; x < blocks.size(); x++) {

				Block b = (Block) blocks.get(x);
				for (int i = 0; i < shots.size(); i++) {

					Shot y = (Shot) shots.get(i);

					y.collidedWith(b, g);
				}

			}

			DevMessage(1, Integer.toString(angle));

			g.dispose();

			strategy.show();

			try {
				Thread.sleep(5);
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

		/// add random spawn into Block definition
		/// overload so that if args are given, they are used, and if not, use random

		// not quite right, should involve the width in x calcs and height for the y

		int brickGap = 40; // 6
		int brickBotGap = 10;
		int brickWidth = 45;// 350;// 35;
		int brickHeight = 25;// 200;// 12;
		int brickLeftMargin = 30;
		int brickTopMargin = 50;
		int brickRowNum = 15;// 1;// 15;
		int brickColNum = 6;// 1;// 5;

//		Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE,Color.GRAY, Color.MAGENTA };

		Color c1, c2, c3, c4, c5, c6, c7;
		c1 = new Color(0, 213, 255);
		c2 = new Color(102, 230, 255);
		c3 = new Color(191, 244, 255);
		c4 = new Color(51, 221, 255);
		c5 = new Color(20, 161, 189);
		c6 = new Color(214, 248, 255);
		c7 = new Color(47, 143, 163);

		Color[] colors = new Color[] { c1, c2, c3, c4, c5, c6, c7 };

		// Color myWhite = new Color(255, 255, 255);
		// create a new Color
		// RGB value of red is 225, 0, 0
		// and set its alpha value as
		// 100 out of 255
		// Color c = new Color(255, 0, 0, 100);

		Random rand = new Random();

		for (int x = 1; x <= brickRowNum; x++) {
			for (int y = 1; y <= brickColNum; y++) {
				int colPick = rand.nextInt(colors.length); // // c = colors[colPick];

				blocks.add(new Block((brickLeftMargin * x) + (brickGap * (x - 1)),
						(brickTopMargin * y) + (brickBotGap * (y - 1)), brickHeight, brickWidth, 101, colors[colPick]));
			}
		}

		/*
		 * for (int x = 1; x <= numBlocks; x++) {
		 * 
		 * Block b = new Block(); blocks.add(b); }
		 */
		/*
		 * blocks.add(new Block(80, 80, 80, 120, 101, Color.white)); blocks.add(new
		 * Block(240, 80, 80, 120, 10, Color.BLUE)); blocks.add(new Block(400, 80, 80,
		 * 120, 10, Color.WHITE)); blocks.add(new Block(540, 80, 80, 120, 10,
		 * Color.GREEN));
		 * 
		 * blocks.add(new Block(80, 200, 80, 120, 20, Color.RED)); blocks.add(new
		 * Block(240, 200, 80, 120, 10, Color.BLUE)); blocks.add(new Block(400, 200, 80,
		 * 120, 10, Color.WHITE)); blocks.add(new Block(540, 200, 80, 120, 10,
		 * Color.GREEN));
		 * 
		 * blocks.add(new Block(80, 320, 80, 120, 20, Color.RED)); blocks.add(new
		 * Block(240, 320, 80, 120, 10, Color.BLUE)); blocks.add(new Block(400, 320, 80,
		 * 120, 10, Color.WHITE)); blocks.add(new Block(540, 320, 80, 120, 10,
		 * Color.GREEN));
		 */

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
			int nw = rand.nextInt(5) * 10;
			int nh = rand.nextInt(12) * 10;
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
			g.fillRect(this.x, this.y, this.width, this.height);

			g.setColor(strength_col);
			g.drawString(Integer.toString(this.strength), this.x, this.y + 10);

			/// temp draw boundaries

			/*
			 * Line2D line_left = new Line2D.Double(); Line2D line_right = new
			 * Line2D.Double(); Line2D line_top = new Line2D.Double(); Line2D line_bottom =
			 * new Line2D.Double();
			 * 
			 * line_left.setLine(this.getX(), this.getY(), this.getX(), this.getY() +
			 * this.getWidth());
			 * 
			 * line_right.setLine(this.getX() + this.getHeight(), this.getY(), this.getX() +
			 * this.getHeight(), this.getY() + this.getWidth());
			 * 
			 * line_top.setLine(this.getX(), this.getY(), this.getX() + this.getHeight(),
			 * this.getY());
			 * 
			 * line_bottom.setLine(this.getX(), this.getY() + this.getWidth(), this.getX() +
			 * this.getHeight(), this.getY() + this.getWidth());
			 */
			/*
			 * g.setColor(Color.RED); g.draw(line_left); g.setColor(Color.WHITE);
			 * g.draw(line_right); g.setColor(Color.BLUE); g.draw(line_top);
			 * g.setColor(Color.GREEN); g.draw(line_bottom);
			 */

			///
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

			int adj_angle = angle - 90;
			//int cannon_rate = 30;

			double rad_angle = adj_angle * Math.PI / 180;
			
			double new_angle = rad_angle; // + cannon_rate;

			int x2 = (int) (length * Math.cos(new_angle)) + orig_x;
			int y2 = (int) (length * Math.sin(new_angle)) + orig_y;

			g.setColor(c);
			g.drawLine(orig_x, orig_y, x2, y2);
			
			fire_arc=12;

			String s = "X = " + Integer.toString(x2) + ", Y = " + Integer.toString(y2) + " a = " + rad_angle;
			s = "angle: " + angle + " fire_arc= " + fire_arc + " rad: " + rad_angle + " final: " + new_angle;
			
			

			//g.drawString(s, x2, y2);
			g.drawString(orig_x + "," + orig_y, orig_x, orig_y);
			DevMessage(2, s);

		}

	}

	private class Shot extends Rectangle {

		private static final long serialVersionUID = 6598889703259797397L;
		private int dx_angle = 1; /*speed*/
		private int dy_angle = 1; /*speed*/
		private int dx = 1; /*angle*/
		private int dy = -3;
		private int shotSize = 10;
		private boolean alive = true;

		private Shot() {

			/*
			 * this.boundary.height = shotSize; this.boundary.width = shotSize;
			 * this.boundary.x = this.x; this.boundary.y = this.y;
			 */

			x = 400;
			y = 500;
			width = shotSize;
			height = shotSize;
		}

		private void draw(Graphics2D g) {

			Ellipse2D ellipse = new Ellipse2D.Double(x, y, shotSize, shotSize);
			g.setColor(Color.RED);
			g.draw(ellipse);
			g.fill(ellipse);

			/*
			 * g.setColor(Color.RED); g.draw(boundary);
			 */

		}

		private void move() {

			if (x > FRAME_WIDTH - shotSize || x < 0) {
				dx = -dx;
			}
			if (y < 0) {
				dy = -dy;
			}

			if (y > FRAME_HEIGHT - shotSize) {
				alive = false;
			}

			x = x + (dx * dx_angle);
			y = y + (dy * dy_angle);
			/*
			 * this.boundary.x = this.x; this.boundary.y = this.y;
			 */

		}

		private void collidedWith(Block b, Graphics2D g) {
			// boolean blReturn=false;

			if (this.intersects(b)) {

				b.strength = b.strength - 1;

				/*
				 * Random rand = new Random(); int dirPick = rand.nextInt(3); // // c =
				 * colors[colPick]
				 * 
				 * switch (dirPick) { case 0: dx = -dx; break; case 1: dy = -dy; case 2: dx =
				 * -dx; dy = -dy; } b.col = Color.yellow;
				 */

				// Determine which side of the box the shot hit

				Line2D line_left = new Line2D.Double();
				Line2D line_right = new Line2D.Double();
				Line2D line_top = new Line2D.Double();
				Line2D line_bottom = new Line2D.Double();

				line_left.setLine(b.getX(), b.getY(), b.getX(), b.getY() + b.getHeight());

				line_right.setLine(b.getX() + b.getWidth(), b.getY(), b.getX() + b.getWidth(),
						b.getY() + b.getHeight());

				line_top.setLine(b.getX(), b.getY(), b.getX() + b.getWidth(), b.getY());

				line_bottom.setLine(b.getX(), b.getY() + b.getHeight(), b.getX() + b.getWidth(),
						b.getY() + b.getHeight());

				g.draw(line_right);
				g.draw(line_left);
				g.draw(line_bottom);
				g.draw(line_top);

				if (this.intersectsLine(line_left)) {
					this.dx = -dx;
					this.x = b.x - this.width - 5;
				}
				if (this.intersectsLine(line_right)) {
					this.dx = -dx;
					this.x = b.x + b.width + 5;
				}
				if (this.intersectsLine(line_top)) {
					this.dy = -dy;
					this.y = b.y - this.height - 5;

				}
				if (this.intersectsLine(line_bottom)) {
					this.dy = -dy;
					this.y = b.y + b.height + 5;
				}

			}

		}

	}

	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			keyWasPressed = true;

//			DevMessage(Integer.toString(e.getKeyCode()));

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
				
				//angle--;
				angle=roundAngle(angle-cannon_rotate_rate);

			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
//				angle++;
				angle=roundAngle(angle+cannon_rotate_rate);

			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (shotsStillInAir == false) {
					firePressed = true;
					shotsStillInAir = true;
				}
			}
		}

		/*
		 * public void keyReleased(KeyEvent e) {
		 * 
		 * if (e.getKeyCode() == KeyEvent.VK_LEFT) { leftPressed = false; } if
		 * (e.getKeyCode() == KeyEvent.VK_RIGHT) { rightPressed = false; } if
		 * (e.getKeyCode() == KeyEvent.VK_SPACE) { // firePressed = false; } }
		 */

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

	private int roundAngle(int a) {
		
		int intReturn = a;
		
		if (a < 0) {
			intReturn = 359;
		}
		if (a > 360) {
			intReturn = 1;
		}
		return intReturn;

	}

}

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
 * Some blocks capture the shot; others give random spin to it
 * 
 */