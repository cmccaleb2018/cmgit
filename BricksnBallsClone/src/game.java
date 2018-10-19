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

	private BufferStrategy strategy;

	public char keytyped;

	private boolean gamerun = true;

	private ArrayList<Block> blocks = new ArrayList<Block>();

	private int numBlocks = 10;

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
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			// draw blocks at new location

			for (int i = 0; i < blocks.size(); i++) {
				Block b = (Block) blocks.get(i);

				g.setColor(b.col);
				g.fillRect(b.x, b.y, b.height, b.width);
			}

			g.dispose();
			strategy.show();

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

/*		Random rand = new Random();
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
		}*/

		Color[] colors = new Color[] { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE,
				Color.GRAY, Color.MAGENTA };

/*//		int colPick = rand.nextInt(colors.length);
//
//		c = colors[colPick];
*/
		
		for (int x=1;x<=numBlocks;x++)
		{
			Block b = new Block();
			b.x = 100;
			b.y = 120;
			b.height = 120;
			b.width = 80;
			b.strength = 10;
			b.col = Color.red;

			blocks.add(b);
		
		}
		
	}

	private class Block extends Rectangle {

		private static final long serialVersionUID = 1L;

		private Color col;
		private int strength;

		private Block() {
			Rectangle r = new Rectangle();

		}

	}

	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

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

		public void keyReleased(KeyEvent e) {

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

		public void keyTyped(KeyEvent e) {

			keytyped = e.getKeyChar();

			// if we hit escape, then quit the game

			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}

}
