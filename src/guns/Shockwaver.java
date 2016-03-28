package guns;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;
import general.Sound;
import processing.core.PApplet;

public class Shockwaver extends PlayerGun {
	public Shockwaver() {
		super(5, 50);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			for (int i = 0; i < 360; i++) {
				Bullet b = new Bullet(player.locCopy(), 5, 20, PApplet.radians(i), 0.02);
				player.addBullet(b);
			}
			Sound.playShockwave();
			return true;
		}
		return false;
	}
}
