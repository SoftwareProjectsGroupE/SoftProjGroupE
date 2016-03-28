package guns;

import entities.creatures.players.Player;
import general.Game;
import general.Sound;
import multiplayer.GameMP;

public class ShotgunMP extends PlayerGun {
	public ShotgunMP() {
		super(1, 100);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			for (int i = 0; i < 10; i++) {
				double spread = player.getAngle() + Math.random() * 0.5 - 0.25;
				double speed = 7 + Math.random() * 3;
				((GameMP) game).send_bullet_fired(2, speed, spread);
			}
			Sound.playArrowFired();
			return true;
		}
		return false;
	}
}
