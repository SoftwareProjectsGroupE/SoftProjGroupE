package entities.creatures.enemies;

import entities.bullets.ExplosionAnimation;
import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import general.Level;
import guns.EnemyAutomatic;
import guns.EnemyFlamethrower;
import guns.EnemyGun;
import guns.EnemyLaser;
import guns.EnemyRocketLauncher;
import guns.EnemyShockwaver;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Boss1 extends Enemy {

	private EnemyGun[] guns = new EnemyGun[5];
	private int current_gun = 0;

	private int state = 0;

	public Boss1(double health, PVector loc, int radius, double speed, double cd) {
		super(health, loc, radius, speed, cd);
		
		guns[0] = new EnemyAutomatic(10, 0.05, 0.5);
		guns[1] = new EnemyRocketLauncher(40, 0.1);
		guns[2] = new EnemyFlamethrower(0.001, 0.1, 6, 4);
		guns[3] = new EnemyShockwaver(0.2, 30);
		guns[4] = new EnemyLaser(0.05);
		
		setPushable(false);
	}

	@Override
	public void update(Game game) {
		Player p = game.getPlayer();

		if (Math.random() < 0.01)
			state = (int) (Math.random() * 3);

		if (state == 0) {
			move(game.get_map(), p.locCopy());
			if (p.loc().dist(loc()) < 100)
				state = 1;
		}

		else if (state == 1) {
			current_gun = (int) (Math.random() * guns.length - 1);
			state = -1;
		}

		else if (state == -1) {
			setAngle(angleTo(p));
			guns[current_gun].shoot((GameSP) game, this);
		}

		else if (state == 2) {
			setAngle(Math.random() * PApplet.TAU);
			state = -2;
		}

		else if (state == -2) {
			incrmnt_angl(0.01);
			guns[4].shoot((GameSP) game, this);
		}
	}

	@Override
	public void render(PGraphics p, PImage img) {
		p.fill(255);
		p.pushMatrix();
		p.translate(locX(), locY());
		p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 300);
		p.rotate((float) getAngle());
		p.popMatrix();
	}

	@Override
	public void onRemove(Game game) {
		GameSP g = game.asSP();
		ExplosionAnimation e = new ExplosionAnimation(loc(), 400, 80);
		g.get_explosions().add(e);
		g.getCamera().setRumble(60);
		g.setFlashTimeout(10);
		Level level = g.getLevel();
		for (int i = 0; i < 20; i++)
			level.addEnemy(new Crawler(0.1, locCopy(), 10, 2 + Math.random() * 2, -0.05));
		super.onRemove(game);
	}

	@Override
	public void onPlayerCollision(GameSP game, Player player) {}

	@Override
	public void onWallCollision() {
		state = 0;
	}
	
	// always update the boss even if it's off screen so override this
	@Override
	public boolean offScreen(PVector screenLoc, int offset) {
		return false;
	}
}
