package workJavaTest;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class TestClass extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates an TestClass object.
	 **/
	public static void main(String args[]) {
		new TestClass();
		
		
	}
// comment added after first commit
	// comment added after second commit
	public TestClass() {

		super("Java 2D Example01");
		setSize(400, 300);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	/*
	 * ...the Java runtime constructs a rendering object and passes it to paint()
	 * 
	 * from:
	 * 
	 * https://www.javaworld.com/article/2076715/java-se/getting-started-with-java-
	 * 2d.html
	 * 
	 */

	public void paint(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(50, 50, 200, 200);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.blue);
		g2d.drawRect(75, 75, 300, 200);

		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path.moveTo(0.0f, 0.0f);
		path.lineTo(0.0f, 125.0f);
		path.lineTo(225.0f, 125.0f);
		path.lineTo(225.0f, 0.0f);
		path.closePath();

		AffineTransform at = new AffineTransform();
		at.setToRotation(-Math.PI / 8.0);
		g2d.transform(at);
		at.setToTranslation(50.0f, 200.0f);
		g2d.transform(at);

		g2d.setColor(Color.green);
		g2d.fill(path);

		/* new */

		// This time our GeneralPath will have nonlinear
		// segments, one second-order (quadratic) and another
		// third-order (cubic). We translate and rotate this shape
		// again, as we did before.
		GeneralPath path2 = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		path2.moveTo(0.0f, 0.0f);
		path2.lineTo(0.0f, 125.0f);
		path2.quadTo(100.0f, 100.0f, 225.0f, 125.0f);
		path2.curveTo(260.0f, 100.0f, 130.0f, 50.0f, 225.0f, 0.0f);
		path2.closePath();

		Graphics2D g3 = (Graphics2D) g;
		AffineTransform at2 = new AffineTransform();
		at2.setToRotation(-Math.PI / 8.0);
		g3.transform(at2);
		at2.setToTranslation(50.0f, 200.0f);
		g3.transform(at2);

		g3.setColor(Color.red);
		g3.fill(path2);

	}
/*	
public void aaa (Graphics x)
{
	Frame frm = new Frame();
	frm.paint(g);
	
	}*/
}