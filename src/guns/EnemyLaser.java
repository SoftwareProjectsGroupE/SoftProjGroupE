package guns;

import entities.bullets.Bullet;
import entities.bullets.LaserBeam;
import entities.creatures.enemies.Enemy;
import entities.creatures.players.Player;
import general.GameSP;
import general.Sound;
import guns.EnemyGun;
import map.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class EnemyLaser extends EnemyGun {

	public EnemyLaser(double damage) {
		super(damage);
	}

	@Override
	public void shoot(GameSP game, Enemy enemy) {

		double a = enemy.getAngle();
		PVector dir = new PVector((float) Math.cos(a), (float) Math.sin(a));
		dir.setMag(1);
		
		Map map = game.getLevel().getMap();
		Player player = game.getPlayer();
		
		final Bullet dummy = new Bullet(enemy.locCopy(), 0, 0, 0, 0);
		
		outer: 
		while (!map.solid(dummy.loc()) && dummy.onScreen(game.screen_loc())) {
			
			for (Bullet b : player.getBullets()) {
				if (b.collides(dummy)) {
					b.onRemove(game);
					break outer;
				}
			}
			
			if (player.collides(dummy)) 
				break outer;
			
			dummy.loc().add(dir);
		}
		
		Bullet b = new LaserBeam(enemy.locCopy(), dummy.locCopy(), damage);
		enemy.addBullet(b);
		
		if (enemy.onScreen(game.screen_loc()))
			Sound.playLaser();
	}
}
