package entities.creatures.enemies;

import processing.core.PVector;

public class EnemyFactory {

	public Enemy randomEnemy(PVector loc) {
		int r = (int) (Math.random() * 3);
		if(r == 0)
			return new StandardEnemy(1, loc, 16, 1 + Math.random(), -0.01);
		if(r == 1)
			return new RandomStateEnemy(1, loc, 16, 3, -0.01);
		if(r == 2)
			return new SuicideBomber(1, loc, 24, 2, -0.1);
		return null;
	}
	
	public int bossCount = -1;
	
	public Enemy nextBoss(PVector loc) {
		bossCount++;
		if (bossCount == 0) 
			return new Boss1(50, loc, 50, 10, -0.03);
		if (bossCount == 1) 
			return new Boss1(20, loc, 50, 6, -0.03);
		if (bossCount == 2) 
			return new Boss1(20, loc, 50, 6, -0.03);
		if (bossCount == 3) 
			return new Boss1(20, loc, 50, 6, -0.03);
		return null;
	}
}
