package guns;

import entities.creatures.enemies.Enemy;
import general.GameSP;

public abstract class EnemyGun {
	final double damage;

	public EnemyGun(double damage) {
		this.damage = damage;
	}

	public abstract void shoot(GameSP game, Enemy enemy);
}