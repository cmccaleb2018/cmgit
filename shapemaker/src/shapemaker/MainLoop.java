
package shapemaker;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Canvas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import java.util.Random;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Random;


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
	
	private char keytyped;
			

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
				System.out.println(me);
				System.out.println(MouseInfo.getPointerInfo().getLocation());
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

		while (goLoop) {

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			Point p = MouseInfo.getPointerInfo().getLocation();

			Double x = p.getX();
			Double y = p.getY();

//			DevMessage("x = " + x + "; y = " + y);
			DevMessage(Character.toString(keytyped));
			
			g.dispose();
			strategy.show();
		}

	}

	

	private class KeyInputHandler extends KeyAdapter {
		/**
		 * The number of key presses we've had while waiting for an "any key" press
		 */
		private int pressCount = 1;

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
			if (waitingForKeyPress) {
				return;
			}

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
			if (waitingForKeyPress) {
				return;
			}

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

			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed

					// event we can mark it as such and start

					// our new game

					waitingForKeyPress = false;
					TheLoop();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}

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

}
