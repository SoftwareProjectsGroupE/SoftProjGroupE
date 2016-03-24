package guns;

import entities.bullets.Bullet;
import entities.bullets.EnemyRocket;
import entities.creatures.enemies.Enemy;
import general.GameSP;
import general.Main;

public class EnemyRocketLauncher extends EnemyGun {

	final int rate;

	public EnemyRocketLauncher(int rate, double damage) {
		super(damage);
		this.rate = rate;
	}

	@Override
	public void shoot(GameSP game, Enemy enemy) {
		if (Main.frameCount % rate == 0) {
			Bullet b = new EnemyRocket(game.getPlayer().loc(), enemy.locCopy(), 2, damage);
			enemy.addBullet(b);
		}
	}
}
