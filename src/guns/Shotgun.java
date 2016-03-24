package guns;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;

public class Shotgun extends PlayerGun {
	public Shotgun() {
		super(1, 100);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			for (int i = 0; i < 10; i++) {
				double spread = player.getAngle() + Math.random() * 0.5 - 0.25;
				double speed = 7 + Math.random() * 3;
				Bullet b = new Bullet(player.locCopy(), 2, speed, spread, 0.1);
				player.addBullet(b);
			}
			return true;
		}
		return false;
	}
}
