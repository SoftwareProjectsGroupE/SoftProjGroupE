package entities.creatures.enemies;

import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import general.Main;
import general.Sprites;
import guns.EnemyAutomatic;
import guns.EnemyFlamethrower;
import guns.EnemyGun;
import guns.EnemyLaser;
import guns.EnemyRocketLauncher;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class RandomStateEnemy extends Enemy {

	private final EnemyGun[] guns = new EnemyGun[4];
	private int current_gun = 0;

	public RandomStateEnemy(PImage img, double health, PVector loc, int radius, double speed, double collision_damage) {
		super(img, health, loc, radius, speed, collision_damage);

		guns[0] = new EnemyAutomatic(30, 0.05, 0.05);
		guns[1] = new EnemyRocketLauncher(60, 0.1);
		guns[2] = new EnemyFlamethrower(0.001, 0.2, 4, 3);
		guns[3] = new EnemyLaser(0.005);
	}

	private int state = 0;

	public void update(Game game) {
		Player player = game.getPlayer();

		if (Math.random() < 0.01)
			state = (int) (Math.random() * 3);

		if (state == 0) {
			setAngle(Math.random() * PApplet.TAU);
			state = -1;
		} else if (state == -1) {
			double s = getSpeed();
			double f = Main.frameRate;
			if (f < Main.MIN_FPS)
				f = Main.MIN_FPS;
			s *= 60.0 / f;
			followAngle(s);
		} else if (state == 1) {
			current_gun = (int) (Math.random() * guns.length - 1);
			state = -2;
		} else if (state == -2) {
			setAngle(angleTo(player));
			guns[current_gun].shoot(game.asSP(), this);
		} else if (state == 2) {
			setAngle(Math.random() * PApplet.TAU);
			state = -3;
		} else if (state == -3) {
			incrmnt_angl(0.005);
			guns[3].shoot(game.asSP(), this);
		} else if (state == -4) {
			move(game.get_map(), player.locCopy());
		}
	}

	@Override
	public void render(PGraphics p) {
		p.stroke(0);
		p.strokeWeight(1);
		p.fill(0, 255, 0);
		p.pushMatrix();
		p.translate(locX(), locY());
		renderHealthBar(p, 50);
		// PImage img = Sprites.get(direction());
		// p.image(img, -img.width/2, -img.height/2);
		//p.box(diameter());
		p.rotate((float) getAngle() + PApplet.radians(90));
		p.image(img, -img.width/2, -img.height/2);
		p.popMatrix();
	}

	@Override
	public void onPlayerCollision(GameSP game, Player p) {
	}

	@Override
	public void onWallCollision() {
		state = -4;
	}
}
