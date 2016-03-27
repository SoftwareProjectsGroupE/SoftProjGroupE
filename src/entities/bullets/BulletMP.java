package entities.bullets;

import general.Game;
import general.Main;
import processing.core.PImage;
import processing.core.PVector;

public class BulletMP extends Bullet {
	
	public final int SHOOTER_ID;

	public BulletMP(PImage img, int shooterID, PVector loc, int radius, double speed, double angle, double damage) {
		super(img, loc, radius, speed, angle, damage);
		SHOOTER_ID = shooterID;
	}
	
	@Override 
	public void update(Game game) {
		// Move the bullet in the direction of its angle.
		double s = getSpeed();
		
		double f = Main.frameRate;
		
		if(f < Main.MIN_FPS)
			f = Main.MIN_FPS;
		
		s *= 60.0/f;
		
		followAngle(s);
	}
}
