package guns;

import entities.creatures.players.Player;
import general.Game;
import general.Main;
import multiplayer.GameMP;

public class FlamethrowerMP extends PlayerGun {
	public FlamethrowerMP() {
		super(4, 1);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (Main.mousePressed) {
			for (int i = 0; i < 2; i++) {
				double spread = player.getAngle() + Math.random() * 0.2 - 0.1;
				double speed = 7 + Math.random() * 3;
				((GameMP) game).send_flame(speed, spread);
			}
			return true;
		}
		return false;
	}
}