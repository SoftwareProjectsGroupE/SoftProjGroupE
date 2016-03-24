package entities;

import general.Game;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class MobileEntity extends Entity {
	
	private double speed;
	private double angle;

	public MobileEntity(PVector loc, int rad, double angle, double speed) {
		super(loc, rad);
		this.speed = speed;
		this.angle = angle;
	}
	
	public abstract void update(Game game);
	
	public void followAngle(double speed) {
		setLocX((locX() + Math.cos(angle) * speed));
		setLocY((locY() + Math.sin(angle) * speed));
	}

	public void followAngle() {
		followAngle(this.speed);
	}

	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double s) {
		speed = s;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double a) {
		angle = a;
	}
	
	public void incrmnt_angl(double a) {
		angle += a;
	}
}
