/// wild alien - just add it to entities alone, so it can be removed? 
// check for collision with ship and wild alien?
/// error after all aliens are kilt - random bound must be positive

/// go ahead and just find a gif for barriers; add barriers and some way to take damage, change color as they do 

package spaceInvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
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
import java.util.TimerTask;

/**
 * The main hook of our game. This class with both act as a manager for the
 * display and central mediator for the game logic.
 * 
 * Display management will consist of a loop that cycles round all entities in
 * the game asking them to move and then drawing them in the appropriate place.
 * With the help of an inner class it will also allow the player to control the
 * main ship.
 * 
 * As a mediator it will be informed when entities within our game detect events
 * (e.g. alient killed, played died) and will take appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1414677499110186137L;
	/** The strategy that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/**
	 * The list of all the entities that exist in our game Added <Entity> as I was
	 * getting 'references to generic type should be paramaterized error
	 */

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	/** The list of entities that need to be removed from the game this loop */

	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	/** The entity representing the player */
	private Entity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 300;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 300;
	/** The number of aliens left on the screen */
	private int alienCount;

	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	/**
	 * True if game logic needs to be applied this loop, normally as a result of a
	 * game event
	 */
	private boolean logicRequiredThisLoop = false;

	/** The number of invader shots to one ship shot */
	private int numInvadershots = 2;

	private static int FRAME_WIDTH = 800;
	private static int FRAME_HEIGHT = 600;

	/** The list of wild invaders entities */
	private ArrayList<Entity> wildList = new ArrayList<Entity>();

	/** The interval in seconds for wild invader fire */
	private int wildfireinterval = 1;
	private int wildfireinterval_milli = wildfireinterval * 100;
	private int wildfireinterval_inc = 0;
	private int wildspawninterval_inc = 0;
	private int wildspawninterval = 3;
	private int wildspawninterval_milli = wildspawninterval  * 100; 
	
	public int score=0;

	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain our game

		JFrame container = new JFrame("Space Invaders 101");

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

		// request the focus so key events come to us

		requestFocus();

		// create the buffering strategy which will allow AWT

		// to manage our accelerated graphics

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialize the entities in our game so there's something
		// to see at startup

		initEntities();

	}

	/**
	 * Start a fresh game, this should clear out any old data and create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set

		entities.clear();
		wildList.clear();
		initEntities();

		// blank out any keyboard settings we might currently have

		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}

	/**
	 * Initialize the starting state of the entities (ship and aliens). Each
	 * entities will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the
		// screen

		ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
		entities.add(ship);

		// create a block of aliens (5 rows, by 12 aliens, spaced evenly)

		alienCount = 0;
		for (int row = 0; row < 5; row++) {
			for (int x = 0; x < 12; x++) {
				Entity alien = new AlienEntity(this, "sprites/invader_sm.gif", 100 + (x * 50), (50) + row * 30);
				entities.add(alien);
				alienCount++;
			}
		}

/*		WildAlien2 wild = new WildAlien2(this, "sprites/invader_sm.gif", 300, 10);
		entities.add(wild);
		wildList.add(wild);*/

		/// add a block here?
		// AlienEntity shield = new AlienEntity(null, message, alienCount, alienCount);
		
		SpawnWild();

	}

	public void DrawGrid(Graphics2D g, int scale) {
		g.setColor(Color.blue);

		// horizontals
		int num = FRAME_WIDTH / scale;
		for (int x = 0; x < num; x++) {
			g.drawLine(0, x * scale, FRAME_WIDTH, x * scale);
		}

		// verticals
		num = FRAME_HEIGHT / scale;
		for (int x = 0; x < num; x++) {
			g.drawLine(x * scale, 0, x * scale, FRAME_HEIGHT);
		}

	}

	/**
	 * Notification from a game entity that the logic of the game should be run at
	 * the next opportunity (normally as a result of some game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	/**
	 * Remove an entity from the game. The entity removed will no longer move or be
	 * drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	/**
	 * Notification that the player has died.
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true;
	}

	/**
	 * Notification that the player has won since all the aliens are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";
		waitingForKeyPress = true;
	}

	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!

		alienCount--;

		if (alienCount == 0) {
			notifyWin();
		}

		// if there are still some aliens left then they all need to get faster,
		// so

		// speed up all the existing aliens

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);

			if (entity instanceof AlienEntity) {
				// speed up by 2% = 1.02

				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02); // 1.02);
			}
		}
	}

	/**
	 * Attempt to fire a shot from the player. Its called "try" since we must first
	 * check that the player can fire at this point, i.e. has he/she waited long
	 * enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire

		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}

		// if we waited long enough, create the shot entity, and record the
		// time.

		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", ship.getX() + 10, ship.getY() - 30);
		entities.add(shot);

		// pick random invader(s) and have them return fire

		/*
		 * for (int x = 0; x < numInvadershots; x++) {
		 * 
		 * Random rand = new Random(); Integer n = rand.nextInt(alienCount) + 1;
		 * 
		 * Entity shooter = (Entity) entities.get(n);
		 * 
		 * InvaderShot ishot = new InvaderShot(this, "sprites/shot.gif", shooter.getX(),
		 * shooter.getY()); // System.out.println(n.toString()); entities.add(ishot); }
		 */
	}

	/**
	 * The main game loop. This loop is running during all game play as is
	 * responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves - Moving the game
	 * entities - Drawing the screen contents (entities, text) - Updating game
	 * events - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();

		// keep looping round til the game ends

		while (gameRunning) {
			// work out how long its been since the last update, this

			// will be used to calculate how far the entities should

			// move this loop

			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// Get hold of a graphics context for the accelerated

			// surface and blank it out

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			// cycle round asking each entity to move itself

			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);

					entity.move(delta);
				}
			}

			// cycle round drawing all the entities we have in the game

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);

				entity.draw(g);
			}

//			DrawGrid(g, 50);

//			DrawLineTo10(g);

			// brute force collisions, compare every entity against

			// every other entity. If any of them collide notify

			// both entities that the collision has occurred

			for (int p = 0; p < entities.size(); p++) {
				for (int s = p + 1; s < entities.size(); s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);

					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}

			// remove any entity that has been marked for clear up

			entities.removeAll(removeList);
			wildList.removeAll(removeList);
			removeList.clear();

			// if a game event has indicated that game logic should

			// be resolved, cycle round every entity requesting that

			// their personal logic should be considered.

			if (logicRequiredThisLoop) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}

				logicRequiredThisLoop = false;
			}

			// if we're waiting for an "any key" press then draw the
			// current message

			if (waitingForKeyPress) {
				g.setColor(Color.white);
				g.drawString(message, (800 - g.getFontMetrics().stringWidth(message)) / 2, 250);
				g.drawString("Press any key", (800 - g.getFontMetrics().stringWidth("Press any key")) / 2, 300);
				wildfireinterval_inc = 0;
			}

			// finally, we've completed drawing so clear up the graphics

			// and flip the buffer over
			DevMessage(Integer.toString(wildfireinterval_inc));
			PostScore(Integer.toString(score));

			g.dispose();
			strategy.show();

			// resolve the movement of the ship. First assume the ship

			// isn't moving. If either cursor key is pressed then

			// update the movement

			ship.setHorizontalMovement(0);

			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);
			}

			// if we're pressing fire, attempt to fire

			if (firePressed) {
				tryToFire();
			}

			// The wild alien fires every wildfireinterval seconds

			wildfireinterval_inc++;

			if (wildfireinterval_inc > wildfireinterval_milli) {
				WildShoots();
				wildfireinterval_inc = 0;
			}

			// spawn a wild alien / replace one of the others every wildspawninterval seconds
			wildspawninterval_inc++;
			if (wildspawninterval_inc > wildspawninterval_milli) {
				SpawnWild();
				wildspawninterval_inc = 0;
			}

			// finally pause for a bit. Note: this should run us at about

			// 100 fps but on windows this might vary each loop due to

			// a bad implementation of timer

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * A class to handle keyboard input from the user. The class handles both
	 * dynamic input during game play, i.e. left/right and shoot, and more static
	 * type input (i.e. press any key to continue)
	 * 
	 * This has been implemented as an inner class more through habbit then anything
	 * else. Its perfectly normal to implement this as seperate class if slight less
	 * convienient.
	 * 
	 * @author Kevin Glass
	 */
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

			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed

					// event we can mark it as such and start

					// our new game

					waitingForKeyPress = false;
					startGame();
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

	/**
	 * The entry point into the game. We'll simply create an instance of class which
	 * will start the display and game loop.
	 * 
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		Game g = new Game();

		// Start the main game loop, note: this method will not

		// return until the game has finished running. Hence we are

		// using the actual main thread to run the game.

		g.gameLoop();
	}

	public void DrawLineTo10(Graphics2D g) {
		Entity entity = (Entity) entities.get(10);
		Entity ship = (Entity) entities.get(0);

		int x = entity.getX();
		int y = entity.getY();

		int ship_x = ship.getX();
		int ship_y = ship.getY();

		g.setColor(Color.red);
		g.drawLine(x + 20, y + 20, ship_x, ship_y);

	}

	public void DevMessage(String s) {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.drawString(s, 5, 590);

	}
	
	public void PostScore(String s) {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.red);
		g.drawString("Score = " + s, 20, 20);

	}

	public void WildShoots() {

		for (int i = 0; i < wildList.size(); i++) {
			Entity wild = (Entity) wildList.get(i);
			InvaderShot ishot = new InvaderShot(this, "sprites/shot.gif", wild.getX() + 10, wild.getY() + 10);
			// System.out.println(n.toString());
			entities.add(ishot);
		}
	}

	public void SpawnWild() {

		Random rand = new Random();
		Integer n = rand.nextInt(alienCount) + 1;

		Entity shooter = (Entity) entities.get(n);
		removeList.add(shooter);
		WildAlien2 wild = new WildAlien2(this, "sprites/invader_sm.gif", shooter.getX(), shooter.getY());
		
		entities.add(wild);
		wildList.add(wild);
		
		// System.out.println(n.toString());
		

	}
}
/*
 * Total 450 Lines of Code. Source code formatted using showsrc by William
 * Denniss
 */