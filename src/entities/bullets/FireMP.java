package entities.bullets;

import general.Game;
import general.Sound;
import processing.core.PGraphics;
import processing.core.PVector;

public class FireMP extends BulletMP {
	
	private final Fire fire;

	public FireMP(int shooter_id, PVector loc, int radius, double speed, double angle, double damage) {
		super(null, shooter_id, loc, radius, speed, angle, damage);
		fire = new Fire(loc, radius, speed, angle, damage);
		Sound.playFlamethrower();
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
	public void render(PGraphics p) {
		fire.render(p);
	}
}
