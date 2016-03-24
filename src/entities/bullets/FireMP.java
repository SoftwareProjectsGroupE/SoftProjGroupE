package entities.bullets;

import general.Game;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class FireMP extends BulletMP {
	
	private final Fire fire;

	public FireMP(int shooter_id, PVector loc, int radius, double speed, double angle, double damage) {
		super(shooter_id, loc, radius, speed, angle, damage);
		fire = new Fire(loc, radius, speed, angle, damage);
	}
	
	@Override
	public int getRadius() {
		return fire.getRadius();
	}
	
	@Override
	public void update(Game game) {
		fire.update(game);
	}
	
	@Override
	public void render(PGraphics p, PImage img) {
		fire.render(p, img);
	}
}
