package guns;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;
import general.Main;

public class Uzi extends PlayerGun {
	public Uzi() {
		super(2, 10);
	}

	@Override
	public boolean shoot(Game game) {
		if (Main.mousePressed && Main.frameCount % 3 == 0) {
			Player player = game.getPlayer();
			double spread = player.getAngle() + Math.random() * 0.3 - 0.15;
			Bullet b = new Bullet(player.locCopy(), 3, 14, spread, 0.2);
			player.addBullet(b);
			return true;
		}
		return false;
	}
}
