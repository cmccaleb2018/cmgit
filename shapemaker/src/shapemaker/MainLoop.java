/* do a thing where dots reach out to other dots and snag them randomly.
or maybe a certain number create a polygon and drops it / stores it somewhere? transforms it to something that rings the edge?
 * 
 */
package shapemaker;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainLoop extends Canvas {

	private static final long serialVersionUID = -7382512584778272034L;
	private static int FRAME_WIDTH = 800;
	private static int FRAME_HEIGHT = 600;

	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean goLoop = true;

	private boolean firePressed = true;
	private boolean rightPressed = true;
	private boolean leftPressed = true;

	private ArrayList<MovingPoint> allPoints = new ArrayList<MovingPoint>();

	public char keytyped;

	private int pntSize = 10;
	private int maxPolyPoints = 4;

	/** The strategy that allows us to use accelerate page flipping */
	private BufferStrategy strategy;

	public MainLoop() {
		JFrame container = new JFrame("Shapes");

		// get hold the content of the frame and set up the resolution of the
		// game

		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame

		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		panel.add(this);

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode

		setIgnoreRepaint(true);

		// finally make the window visible

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed

		addKeyListener(new KeyInputHandler());

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
//				System.out.println(me);

//				System.out.println(me.getX() + " " + me.getY());
				// other MouseEvents: getButton as in:
				// if( e.getButton() == MouseEvent.BUTTON3 )

				/*
				 * point spawn method. add point to array of points with coordinates of mouse
				 * click and redraw from those each frame if there is more than one point, draw
				 * a line shape from each point in order - last point to first drawPolygon()
				 * give points random direction and have them 'float' around screen
				 * 
				 */

				spawnPoint(me.getX(), me.getY());

			}
		});

		// request the focus so key events come to us

		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics

		createBufferStrategy(2);
		strategy = getBufferStrategy();

	}

	public static void main(String[] args) {

		MainLoop ml = new MainLoop();

		ml.TheLoop();

	}

	public void TheLoop() {

//		int z = 0;

		while (goLoop) {

			// z++;

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			Point p = MouseInfo.getPointerInfo().getLocation();

			Double x = p.getX();
			Double y = p.getY();

//			DevMessage("x = " + x + "; y = " + y);
//			DevMessage(Integer.toString(z) + " " + Character.toString(keytyped));

			for (int i = 0; i < allPoints.size(); i++) {
				MovingPoint mp = (MovingPoint) allPoints.get(i);

				mp.move();

				g.setColor(mp.c);
				g.fillOval(mp.x, mp.y, pntSize, pntSize);

			}

			// draw line if 2 points

			if (allPoints.size() == 2) {
				OnlyTwoPoints(g);
			}

			// draw polygon if more than 3 points

			if (allPoints.size() > 2) {

				MoreThanTwoPoints(g);

			}

			g.dispose();
			strategy.show();

//			if (z > 1000) {
//				goLoop = false;
//			}

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}

	}

	private class KeyInputHandler extends KeyAdapter {
		/**
		 * The number of key presses we've had while waiting for an "any key" press
		 */
//		private int pressCount = 1;

		/**
		 * Notification from AWT that a key has been pressed. Note that a key being
		 * pressed is equal to being pushed down but *NOT* released. Thats where
		 * keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "press"
//			if (waitingForKeyPress) {
//				return;
//			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		}

		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "released"
//			if (waitingForKeyPress) {
//				return;
//			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notification from AWT that a key has been typed. Note that typing a key means
		 * to both press and then release it.
		 *
		 * @param e The details of the key that was typed.
		 */
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may

			// have had a keyType() event from the user releasing

			// the shoot or move keys, hence the use of the "pressCount"
			// counter.

			keytyped = e.getKeyChar();

//			if (waitingForKeyPress) {
//				if (pressCount == 1) {
//					// since we've now recieved our key typed
//
//					// event we can mark it as such and start
//
//					// our new game
//
//					waitingForKeyPress = false;
//					TheLoop();
//					pressCount = 0;
//				} else {
//					pressCount++;
//				}
//			}

			// if we hit escape, then quit the game

			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}

	public void DevMessage(String s) {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.drawString(s, 5, 590);

	}

	public void spawnPoint(int x, int y) {

		MovingPoint mp = new MovingPoint(x, y);
		allPoints.add(mp);

	}

	private class MovingPoint extends Point {

		private static final long serialVersionUID = 6360717428310632663L;
		private int dx = 1;
		private int dy = 1;
		private int x;
		private int y;
		private Color c = Color.yellow;
		private boolean dead = false;

		private MovingPoint(int given_x, int given_y) {

			this.x = given_x;
			this.y = given_y;

			Random rand = new Random();
			dx = rand.nextInt(10) + 1;
			if (dx < 6) {
				dx = -1;
			} else {
				dx = 1;
			}
			dy = rand.nextInt(10) + 1;
			if (dy < 6) {
				dy = -1;
			} else {
				dy = 1;
			}

			Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE,
					Color.GRAY, Color.MAGENTA };

			int colPick = rand.nextInt(colors.length);

			c = colors[colPick];

		}

		private void move() {

			if (x > FRAME_WIDTH - pntSize || x < 0) {
				dx = -dx;
			}
			if (y > FRAME_HEIGHT - pntSize || y < 0) {
				dy = -dy;
			}

			x = x + dx;
			y = y + dy;

			if (dead == true && y == FRAME_HEIGHT - pntSize) {
				dy = 0;
			}
		}

	}

	private void OnlyTwoPoints(Graphics2D g) {
		MovingPoint mp = (MovingPoint) allPoints.get(0);
		MovingPoint mp2 = (MovingPoint) allPoints.get(1);
		g.setColor(Color.cyan);
		g.drawLine(mp.x + (pntSize / 2), mp.y + (pntSize / 2), mp2.x + (pntSize / 2), mp2.y + (pntSize / 2));
	}

	private void MoreThanTwoPoints(Graphics2D g) {
		/*
		 * strangely difficult to create a dynamic integer array with the List /
		 * ArrayList method I get the cannot convert from List<Integer> to int[] error
		 * FOr now just limiting to maxPolyPoints points
		 */

//	List<Integer> polyx = new ArrayList<Integer>();
//	List<Integer> polyy = new ArrayList<Integer>();

		int[] polyx = new int[maxPolyPoints];
		int[] polyy = new int[maxPolyPoints];

		int i = allPoints.size() - maxPolyPoints;

		if (i < 0) {
			i = 0;
		} else {
			/*
			 * drop the oldest point on the Polygon to the bottom
			 * 
			
			/// could be items 7 through 10 - need to get 6
			 * with maxPolyPoints of 4, i=6
			 * 
			 */
			

		/*	MovingPoint mp = (MovingPoint) allPoints.get(i);
			mp.dx = 0;
			mp.dy = 1;
			mp.dead = true;*/

		}

		int polyArrayInt = 0;

		for (int b = i; b < allPoints.size(); b++) {

			MovingPoint mp = (MovingPoint) allPoints.get(b);

			polyx[polyArrayInt] = mp.x + (pntSize / 2);
			polyy[polyArrayInt] = mp.y + (pntSize / 2);

			polyArrayInt++;
		}

		Polygon p1 = new Polygon();

		p1.xpoints = polyx;
		p1.ypoints = polyy;
		p1.npoints = polyArrayInt; // allPoints.size();

		g.setColor(Color.green);
		g.drawPolygon(p1);

		/*
		 * g.setColor(Color.green); g.fillPolygon(p1);
		 */ }
}
