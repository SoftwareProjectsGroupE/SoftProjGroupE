package entities.bullets;

import entities.MobileEntity;
import general.Game;
import processing.core.PGraphics;
import processing.core.PVector;

public class Bullet extends MobileEntity {

	public final double DAMAGE;

	public Bullet(PVector loc, int radius, double speed, double angle, double damage) {
		super(loc, radius, angle, speed);
		DAMAGE = damage;
	}

	public void update(Game game) {
		// Move the bullet in the direction of its angle.
		followAngle();
	}

	public void render(PGraphics p) {
		p.stroke(0);
		p.strokeWeight(1);
		p.fill(255, 0, 255);
		p.ellipse(locX(), locY(), diameter(), diameter());
	}
}
