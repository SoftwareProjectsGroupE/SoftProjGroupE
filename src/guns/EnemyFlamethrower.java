package guns;

import entities.bullets.Bullet;
import entities.bullets.Fire;
import entities.creatures.enemies.Enemy;
import general.GameSP;

public class EnemyFlamethrower extends EnemyGun {

	final double spread;
	final double minSpeed, speedRange;

	public EnemyFlamethrower(double damage, double spread, double minSpeed, double speedRange) {
		super(damage);
		this.spread = spread;
		this.minSpeed = minSpeed;
		this.speedRange = speedRange;
	}

	@Override
	public void shoot(GameSP game, Enemy enemy) {
		for (int i = 0; i < 15; i++) {
			double spread = enemy.getAngle() + Math.random() * this.spread - this.spread / 2;
			double speed = minSpeed + Math.random() * speedRange;
			Bullet b = new Fire(enemy.locCopy(), 20, speed, spread, damage);
			enemy.getBullets().add(b);
		}
	}
}
