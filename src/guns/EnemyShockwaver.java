package guns;

import entities.bullets.Bullet;
import entities.creatures.enemies.Enemy;
import general.GameSP;
import general.Main;
import general.Sound;
import processing.core.PApplet;

public class EnemyShockwaver extends EnemyGun {

	final int rate;

	public EnemyShockwaver(double damage, int rate) {
		super(damage);
		this.rate = rate;
	}

	@Override
	public void shoot(GameSP game, Enemy enemy) {
		if (Main.frameCount % rate == 0) {
			for (int i = 0; i < 360; i += 30) {
				Bullet b = new Bullet(enemy.locCopy(), 5, 5, PApplet.radians(i), damage);
				enemy.addBullet(b);
			}
			game.setFlashTimeout(30);
			Sound.playShockwave();
		}
	}
}
