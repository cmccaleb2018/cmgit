package spaceInvaders;

public class WildAlien extends AlienEntity {
	
	private Game game;

	public WildAlien(Game game, String ref, int x, int y) {
		super(game, ref, x, y);
		// TODO Auto-generated constructor stub
		dx = 0;
		dy = -10;
		
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

		if ((x < 0) || (x > 800)) {
			dx = -dx;
		}
		if ((y < 0) || (y > 600)) {
			dy = -dy;
		}
		
//		game.DevMessage("Wild x = " + Math.round(x) + " y = " + Math.round(y));

		super.move(delta);
		
	}
	
//	public void doLogic() {
//		// swap over horizontal movement and move down the
//
//		// screen a bit
//
////		dx = -dx;
////		y += 10;
//		
//		// if we've reached the bottom of the screen then the player
//
//		// dies
//
////		if (y > 570) {
////			game.notifyDeath();
////		}
//	}

}
