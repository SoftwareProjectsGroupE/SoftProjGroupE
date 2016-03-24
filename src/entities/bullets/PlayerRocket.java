package entities.bullets;

import entities.creatures.enemies.Enemy;
import general.Game;
import general.GameSP;
import general.Level;
import general.Main;
import map.Map;
import processing.core.PVector;

public class PlayerRocket extends Rocket {

	public PlayerRocket(PVector dir, PVector loc, double speed, double angle, double damage) {
		super(loc, speed, angle, damage);
		this.dir = dir;
	}

	private final PVector dir;

	private boolean wallBetween(PVector p, Map map) {
		PVector start = locCopy();
		PVector target = p.copy();
		
		PVector dir = PVector.sub(target, start);
		dir.setMag(Map.BLOCK_SIZE - 1);
		
		while (start.dist(target) > Map.BLOCK_SIZE) {
			start.add(dir);
			if (map.solid(start)) 
				return true;
		}
		return false;
	}

	@Override
	public void update(Game game) {
		Level level = ((GameSP) game).getLevel();
		PVector p = game.getPlayer().loc();

		Enemy target = null;
		for (Enemy e : level.getEnemies()) 
			if (e.onScreen(p.x - Main.WIDTH / 2, p.y - Main.HEIGHT / 2)) 
				if (!wallBetween(e.loc(), level.getMap())) 
					if (target == null || loc().dist(e.loc()) < loc().dist(target.loc())) 
						target = e;

		if (target == null) {
			super.update(game);
			return;
		}

		PVector seek = PVector.sub(target.loc(), loc());
		dir.add(seek.setMag((float) (getSpeed() / 4)));
		loc().add(dir.setMag((float) getSpeed()));

		setAngle(dir.heading());
	}
}
