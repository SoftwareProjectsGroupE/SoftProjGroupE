package guns;

import entities.bullets.Bullet;
import entities.creatures.enemies.Enemy;
import general.GameSP;
import general.Main;
import general.Sound;

public class EnemyAutomatic extends EnemyGun {

	private final double spread;
	private final int rate;

	public EnemyAutomatic(int rate, double damage, double spread) {
		super(damage);
		this.rate = rate;
		this.spread = spread;
	}

	@Override
	public void shoot(GameSP game, Enemy enemy) {
		if (Main.frameCount % rate == 0) {
			double angle = enemy.getAngle() + Math.random() * spread - spread / 2;
			Bullet b = new Bullet(enemy.locCopy(), 4, 4, angle, damage);
			enemy.getBullets().add(b);
			if (enemy.onScreen(game.screen_loc())) 
				Sound.playEnemyBullet();
		}
	}
}
