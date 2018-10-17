package spaceInvaders;

public class WildAlien2 extends Entity {
	/** The speed at which the alien moves horizontally */

	/** The game in which the entity exists */
	private Game game;
	
	/**
	 * Create a new alien entity
	 * 
	 * @param game The game in which this entity is being created
	 * @param ref The sprite which should be displayed for this alien
	 * @param x The intial x location of this alien
	 * @param y The intial y location of this alient
	 */
	public WildAlien2(Game game,String ref,int x,int y) {
				
		
		super(ref, x, y);

		
		// TODO Auto-generated constructor stub
		dx = -120;
		dy = 120;
		
		this.game = game;
		
	}

	/**
	 * Request that this alien moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	public void move(long delta) {
		if ((x < 0) || (x > 800)) {
			dx = -dx;
		}
		if ((y < 0) || (y > 600)) {
			dy = -dy;
		}
		super.move(delta);
	}
	
	/**
	 * Update the game logic related to aliens
	 */
	public void doLogic() {
		
	}
	
	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other The other entity
	 */
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere

	}
}
