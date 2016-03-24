package entities.bullets;

import general.Removeable;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class ExplosionAnimation implements Removeable {

	private final Explosion[] explosions;
	private final int spread;

	public ExplosionAnimation(PVector p, int spread, int count) {
		explosions = new Explosion[count];
		this.spread = spread;
		PVector loc = p.copy();
		for (int i = 0; i < explosions.length; i++) 
			explosions[i] = new Explosion(loc);
	}

	public void update() {
		for (Explosion e : explosions) 
			if (e.time < e.timeout)
				e.time++;
		
		animationTimeout++;
	}

	private int animationTimeout = -20;

	@Override
	public boolean removed() {
		return animationTimeout > 20;
	}

	public void render(PGraphics p) {
		for (Explosion e : explosions) {
			int size = (int) PApplet.map(PApplet.abs(e.time), e.timeout, 0, 0, 200);
			p.noStroke();
			float fade = PApplet.map(e.time, -e.timeout, e.timeout, 255, 0);
			p.fill(255, fade, 0, fade);
			p.ellipse(e.loc.x, e.loc.y, size, size);
			p.stroke(0);
		}
	}

	private class Explosion {
		int timeout;
		int time;
		PVector loc;

		Explosion(PVector source) {
			timeout = 10 + (int) (Math.random() * 10);
			time = -timeout;
			float x = (float) (source.x + Math.random() * spread - spread / 2);
			float y = (float) (source.y + Math.random() * spread - spread / 2);
			loc = new PVector(x, y);
		}
	}
}
