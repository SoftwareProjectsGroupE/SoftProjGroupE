package entities.bullets;

import general.Game;
import gui.Utils;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class LaserBeam extends Bullet {
	
	private final PVector start;

	public LaserBeam(PVector start, PVector end, double damage) {
		super(end, 4, 0, 0, damage);
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
	public void render(PGraphics p) {
		p.stroke(Utils.pulse(15.0), 0, Utils.pulse(30.0));
		p.strokeWeight(diameter());
		p.line(locX(), locY(), start.x, start.y);
		p.strokeWeight(0);
		p.fill(Utils.pulse(15.0), 0, Utils.pulse(30.0));
		p.ellipse(locX(), locY(), diameter(), diameter());
		p.strokeWeight(1);
		p.stroke(0);
	}
}
