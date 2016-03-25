package entities.creatures.enemies;

import general.GameConstants;
import processing.core.PVector;

public class EnemyFactory {

	public Enemy randomEnemy(PVector loc) {
		int r = (int) (Math.random() * 3);
		if(r == 0)
			return new StandardEnemy(1, loc, 16, 1 + Math.random(), GameConstants.STANDARD_ENEMY_COLLISION_DAMAGE);
		if(r == 1)
			return new RandomStateEnemy(1, loc, 16, GameConstants.RANDOM_STATE_ENEMY_SPEED, GameConstants.RANDOM_STATE_ENEMY_COLLISION_DAMAGE);
		if(r == 2)
			return new SuicideBomber(1, loc, 24, GameConstants.SUICIDE_BOMBER_SPEED, GameConstants.SUICIDE_BOMBER_COLLISION_DAMAGE);
		return null;
	}
	
	public int bossCount = -1;
	
	public Enemy nextBoss(PVector loc) {
		bossCount++;
		double cd = GameConstants.BOSS_COLLISION_DAMAGE;
		int h = GameConstants.BOSS_HEALTH;
		int s = GameConstants.BOSS_SPEED;
		if (bossCount == 0) 
			return new Boss1(h, loc, 50, s, cd);
		if (bossCount == 1) 
			return new Boss1(h, loc, 50, s, cd);
		if (bossCount == 2) 
			return new Boss1(h, loc, 50, s, cd);
		if (bossCount == 3) 
			return new Boss1(h, loc, 50, s, cd);
		return null;
	}
}
