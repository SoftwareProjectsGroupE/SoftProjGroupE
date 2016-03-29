package entities.creatures.enemies;

import general.GameConstants;
import general.Sprites;
import processing.core.PImage;
import processing.core.PVector;

public class EnemyFactory {

	public Enemy randomEnemy(PVector loc, int level) {
		PImage std;
		PImage rse;
		PImage suic;
		if (level < 3) {
			std = Sprites.DINO_STD;
			rse = Sprites.DINO_RSE;
			suic = Sprites.DINO_SUIC;
		} else if (level > 3 && level < 6) {
			std = Sprites.MEDIEV_STD;
			rse = Sprites.MEDIEV_RSE;
			suic = Sprites.MEDIEV_SUIC;
		} else {
			std = Sprites.UNDR_STD;
			rse = Sprites.UNDR_RSE;
			suic = Sprites.UNDR_SUIC;
		}
		
		int r = (int) (Math.random() * 3);
		if(r == 0)
			return new StandardEnemy(std, 1, loc, 16, 1 + Math.random(), GameConstants.STANDARD_ENEMY_COLLISION_DAMAGE);
		if(r == 1)
			return new RandomStateEnemy(rse, 1, loc, 16, GameConstants.RANDOM_STATE_ENEMY_SPEED, GameConstants.RANDOM_STATE_ENEMY_COLLISION_DAMAGE);
		if(r == 2)
			return new SuicideBomber(suic, 1, loc, 24, GameConstants.SUICIDE_BOMBER_SPEED, GameConstants.SUICIDE_BOMBER_COLLISION_DAMAGE);
		return null;
	}
		
	public Enemy nextBoss(PVector loc, int level) {
		PImage boss;
		PImage crwl;
		if (level == 3) {
			boss = Sprites.DINO_BOSS;
			crwl = Sprites.DINO_CRWL;
		} else if (level == 6) {
			boss = Sprites.MEDIEV_BOSS;
			crwl = Sprites.MEDIEV_CRWL;
		} else if (level == 9) {
			boss = Sprites.UNDR_BOSS;
			crwl = Sprites.UNDR_CRWL;
		} else {
			boss = Sprites.FINL_BOSS;
			crwl = Sprites.FINL_CRWL;
		}
		double cd = GameConstants.BOSS_COLLISION_DAMAGE;
		int h = GameConstants.BOSS_HEALTH;
		int s = GameConstants.BOSS_SPEED;
		if (level == 10) 
			return new Boss(boss, crwl, 200, loc, 16, 10, -0.06);
		return new Boss(boss, crwl, h, loc, 16, s, cd);
	}
}
