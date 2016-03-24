package guns;

import entities.bullets.Bullet;
import entities.bullets.Fire;
import entities.creatures.players.Player;
import general.Game;
import general.GameConstants;
import general.Main;

public class Flamethrower extends PlayerGun {
	public Flamethrower() {
		super(4, GameConstants.FLAMETHROWER_AMMO_DCRMNT_MP);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (Main.mousePressed) {
			for (int i = 0; i < 20; i++) {
				double spread = player.getAngle() + Math.random() * 0.2 - 0.1;
				double speed = 7 + Math.random() * 3;
				Bullet b = new Fire(player.locCopy(), 50, speed, spread, 0.002);
				player.addBullet(b);
			}
			return true;
		}
		return false;
	}
}
