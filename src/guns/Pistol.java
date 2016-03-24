package guns;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;

public class Pistol extends PlayerGun {

	public Pistol() {
		super(0, 0);
	}

	@Override
	public boolean outOfAmmo() {
		return false;
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			Bullet b = new Bullet(player.locCopy(), 5, 10, player.getAngle(), 0.4);
			player.addBullet(b);
			return true;
		}
		return false;
	}
}
