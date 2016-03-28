package guns;

import entities.bullets.Bullet;
import entities.bullets.LaserBeam;
import entities.creatures.enemies.Enemy;
import entities.creatures.players.Player;
import general.Game;
import general.GameConstants;
import general.Level;
import general.Main;
import general.Sound;
import general.GameSP;
import guns.PlayerGun;
import map.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class PlayerLaser extends PlayerGun {

	public PlayerLaser() {
		super(6, GameConstants.LASER_AMMO_DCRMNT_SP);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		
		if (!Main.mousePressed)
			return false;

		Level level = ((GameSP) game).getLevel();
		Map map = level.getMap();
		
		final Bullet dummy = new Bullet(player.locCopy(), 0, 0, 0, 0);
		
		float a = (float) player.getAngle();
		PVector dir = new PVector(PApplet.cos(a), PApplet.sin(a));
		dir.setMag(1);
		
		outer: 
		while (!map.solid(dummy.loc()) && dummy.onScreen(game.screen_loc())) {
			for (Enemy e : level.getEnemies()) {
				
				if (e.collides(dummy)) 
					break outer;
				
				for (Bullet b : e.getBullets()) {
					if (b.collides(dummy)) {
						b.onRemove(game);
						break outer;
					}
				}
			}
			dummy.loc().add(dir);
		}
		
		Bullet b = new LaserBeam(player.locCopy(), dummy.locCopy(), GameConstants.LASER_DAMAGE_SP);
		player.addBullet(b);
		
		Sound.playLaser();
		
		return true;
	}
}
