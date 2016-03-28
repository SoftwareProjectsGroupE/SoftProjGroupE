package guns;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;
import general.Sound;
import general.Sprites;

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
			Bullet b = new Bullet(Sprites.ARROW, player.locCopy(), 5, 10, player.getAngle(), 0.4);
			player.addBullet(b);
			Sound.playArrowFired();
			return true;
		}
		return false;
	}
}
