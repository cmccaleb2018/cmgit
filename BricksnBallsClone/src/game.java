import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

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
	}

	public static void main(String[] args) {

		game g = new game();

		g.initGameItems();

		g.TheLoop();

	}

	private void initGameItems() {
		
		/* add random blocks with random strengths, labeled and w/ colors
		 * need block class w/ color, size, position, strength, extending rectangle
		 * 
		 * 
		 */
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
