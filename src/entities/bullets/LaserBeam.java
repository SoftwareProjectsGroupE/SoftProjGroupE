package entities.bullets;

import general.Game;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class LaserBeam extends Bullet {
	
	private final PVector start;

	public LaserBeam(PVector start, PVector end) {
		super(end, 4, 0, 0, 0.03);
		this.start = start;
	}
	
	private int timeout = 1;
	
	@Override
	public void update(Game game) {
		timeout--;
	}
	
	@Override 
	public boolean removed() {
		return timeout < 0;
	}
	
	@Override
	public void render(PGraphics p, PImage img) {
		p.stroke(0, 255, 0);
		p.strokeWeight(diameter());
		p.line(locX(), locY(), start.x, start.y);
		p.strokeWeight(1);
		p.fill(0, 255, 0);
		p.ellipse(locX(), locY(), diameter(), diameter());
		p.stroke(0);
	}
}
