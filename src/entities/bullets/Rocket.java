package entities.bullets;

import general.Game;
import general.Sound;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Rocket extends Bullet {

	private PVector[] fire = new PVector[20];

	public Rocket(PVector loc, double speed, double angle, double damage) {
		super(loc, 5, speed, angle, damage);
		for (int i = 0; i < fire.length; i++)
			fire[i] = loc.copy();
	}

	public Rocket(PVector loc, double speed, double damage) {
		this(loc, speed, 0, damage);
	}

	@Override
	public void onRemove(Game game) {
		ExplosionAnimation e = new ExplosionAnimation(loc(), 50, 20);
		game.add_explosion(e);
		Sound.playSmallExplosion();
		super.onRemove(game);
	}

	@Override
	public void render(PGraphics p) {
		PVector temp = locCopy();
		temp.x += Math.random() * 10 - 5;
		temp.y += Math.random() * 10 - 5;
		fire[0] = temp;

		for (int i = fire.length - 1; i >= 1; i--) {
			fire[i] = fire[i - 1].copy();
			float fade = PApplet.map(i, fire.length - 1, 1, 0, 255);
			p.fill(255, fade, 0, fade);
			p.noStroke();
			int size = (int) PApplet.map(i, fire.length - 1, 1, 20, 0);
			p.ellipse(fire[i].x, fire[i].y, size, size);
			p.stroke(0);
		}

		p.fill(255, 70, 30);
		p.pushMatrix();
		p.translate(locX(), locY());
		p.rotate((float) getAngle() + PApplet.PI / 2);
		int r = getRadius();
		p.triangle(-r, r, 0, -20, r, r);
		p.popMatrix();
	}
}
