package entities.bullets;

import entities.MobileEntity;
import general.Game;
import general.Main;
import gui.Utils;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Bullet extends MobileEntity {

	public final double DAMAGE;
	
	private PImage img;
	
	public Bullet(PVector loc, int radius, double speed, double angle, double damage) {
		super(loc, radius, angle, speed);
		DAMAGE = damage;
	}

	public Bullet(PImage img, PVector loc, int radius, double speed, double angle, double damage) {
		super(loc, radius, angle, speed);
		DAMAGE = damage;
		this.img = img;
	}

	public void update(Game game) {
		// Move the bullet in the direction of its angle.
        double s = getSpeed();
		
		double f = Main.frameRate;
		
		if(f < Main.MIN_FPS)
			f = Main.MIN_FPS;
		
		s *= 60.0/f;
		followAngle(s);
	}

	public void render(PGraphics p) {
		if (img != null) {
			p.pushMatrix();
			p.translate(locX(), locY());
			p.rotate((float) getAngle() - PApplet.radians(90));
			p.image(img, -img.width/2, -img.height/2);
			p.popMatrix();
			return;
		}
		p.stroke(0);
		p.strokeWeight(1);
		p.fill(Utils.pulse(15.0), 0, Utils.pulse(30.0));
		p.ellipse(locX(), locY(), diameter(), diameter());

	}
}
