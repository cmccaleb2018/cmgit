package workJavaTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Boxes extends JFrame {

	public static final int CANVAS_WIDTH = 600;
	public static final int CANVAS_HEIGHT = 200;
	public static final Color LINE_COLOR = Color.RED;
	public static final Color CANVAS_BACKGROUND = Color.BLACK;

	public static int x1 = CANVAS_WIDTH / 2;
	public static int y1 = CANVAS_HEIGHT / 8;
	public static int x2 = 100; // x1;
	public static int y2 = 100; // CANVAS_HEIGHT / 8 * 7;

	// private APanel jp;

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Boxes(); // Let the constructor do the job
			}
		});
	}

	public void ResetBox() {

		System.out.println(x1 + "," + y1 + "," + x2 + "," + y2);
		x1 = CANVAS_WIDTH / 2;
		y1 = CANVAS_HEIGHT / 8;
		x2 = 100; // x1;
		y2 = 100; // CANVAS_HEIGHT / 8 * 7;
		System.out.println(x1 + "," + y1 + "," + x2 + "," + y2 );

	}

	public Boxes() {

		// JFrame jf = new JFrame();

		setTitle("Boxes");

		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		// jf.setBackground(CANVAS_BACKGROUND);

		APanel jp = new APanel();

		// jp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		cp.add(jp, BorderLayout.CENTER);

		pack();

		setVisible(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					// x1 -= 10;
					x2 -= 10;
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					// x1 += 10;
					x2 += 10;
					repaint();
					break;
				case KeyEvent.VK_UP:
					x1 -= 10;
					// x2 -= 10;
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					x1 += 10;
					// x2 += 10;
					repaint();
					break;
				}
			}
		});

		addMouseListener(new MouseListener() {
			// @Override
			public void mouseClicked(MouseEvent e) {

				ResetBox();
				repaint();

				/*
				 * int x = e.getX(); int y = e.getY();
				 */
				// System.out.println(x + "," + y);

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button

	}

	class APanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(CANVAS_BACKGROUND);
			g.setColor(LINE_COLOR);
			g.fillRect(x1, y1, x2, y2); // Draw the line

		}
	}
}
