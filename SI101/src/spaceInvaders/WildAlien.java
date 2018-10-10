package spaceInvaders;

public class WildAlien extends AlienEntity {
	
	private Game game;

	public WildAlien(Game game, String ref, int x, int y) {
		super(game, ref, x, y);
		// TODO Auto-generated constructor stub
		dx = -75;
		dy = -75;
		
		this.game = game;

	}

	public void move(long delta) {
		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update

//		if ((dx < 0) && (x < 10)) {
//			game.updateLogic();
//		}
		// and vice vesa, if we have reached the right hand side of

		// the screen and are moving right, request a logic update

//		if ((dx > 0) && (x > 750)) {
		// game.updateLogic();
//		}

		// proceed with normal move

		if ((x < 0) || (x > 600)) {
			dx = -dx;
		}
		if ((y < 0) || (y > 800)) {
			dy = -dy;
		}
		
		game.DevMessage("Wild x = " + Math.round(x) + " y = " + Math.round(y));

		super.move(delta);
		
	}

}
