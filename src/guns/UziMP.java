package guns;

import general.Game;
import general.Main;
import multiplayer.GameMP;

public class UziMP extends PlayerGun {
	public UziMP() {
		super(2, 10);
	}

	@Override
	public boolean shoot(Game game) {
		if (Main.mousePressed && Main.frameCount % 3 == 0) {
			double spread = game.getPlayer().getAngle() + Math.random() * 0.3 - 0.15;
			((GameMP) game).send_bullet_fired(3, 14, spread);
			return true;
		}
		return false;
	}
}
