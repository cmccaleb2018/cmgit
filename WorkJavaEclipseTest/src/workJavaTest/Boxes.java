package workJavaTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Boxes extends JFrame{

	public static final int CANVAS_WIDTH = 600;
	public static final int CANVAS_HEIGHT = 200;
	public static final Color LINE_COLOR = Color.RED;
	public static final Color CANVAS_BACKGROUND = Color.BLACK;

	static int x1 = CANVAS_WIDTH / 2;
	static int y1 = CANVAS_HEIGHT / 8;
	static int x2 = x1;
	static int y2 = CANVAS_HEIGHT / 8 * 7;
 
	private APanel jp;

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Boxes(); // Let the constructor do the job
			}
		});
	}

	public Boxes() {

		//JFrame jf = new JFrame();

		setTitle("Boxes");

		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		// jf.setBackground(CANVAS_BACKGROUND);

		jp = new APanel();

		jp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		cp.add(jp,BorderLayout.CENTER);
		
		pack();

		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button

	}


	class APanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(CANVAS_BACKGROUND);
			g.setColor(LINE_COLOR);
			g.drawLine(x1, y1, x2, y2); // Draw the line
		}

	}

}
