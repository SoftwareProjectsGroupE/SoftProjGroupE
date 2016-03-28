package entities.bullets;

import general.Game;
import general.Sound;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Fire extends Bullet {

	private final int MAX_RADIUS;

	private int timeout = (int) (20 + Math.random() * 20);
	private final int start = timeout;

	public Fire(PVector loc, int radius, double speed, double a, double damage) {
		super(loc, radius, speed, a, damage);
		MAX_RADIUS = radius;
		setRadius(0);
		Sound.playFlamethrower();
	}

	@Override
	public void update(Game game) {
		if (--timeout < 0)
			onRemove(game);
		super.update(game);
	}

	@Override
	public void render(PGraphics p) {
		float radius = PApplet.map(timeout, start, 0, 0, MAX_RADIUS);
		setRadius((int) radius);
		float fade = PApplet.map(timeout, start, 0, 255, 0);
		p.fill(255, fade, 0, fade);
		p.noStroke();
		p.ellipse(locX(), locY(), diameter(), diameter());
		p.stroke(0);
	}
}
