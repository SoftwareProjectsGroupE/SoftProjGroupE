package entities.creatures.players;

import general.Camera;
import general.Game;
import general.Main;
import general.Sprites;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PlayerSP extends Player {

	public PlayerSP(double health, PVector loc, int radius, float speed) {
		super(health, loc, radius, speed);
	}

	public void update(Game game) {
		if (speedBoostTimeout > -1)
			if (--speedBoostTimeout == 0)
				setSpeed(ORIGINAL_SPEED);

		Camera c = game.asSP().getCamera();
		float ox = locX() - c.locX();
		float oy = locY() - c.locY();
		double a = Math.atan2(Main.mouseY - oy - Main.HEIGHT / 2, Main.mouseX - ox - Main.WIDTH / 2);
		setAngle(a);
		updateLoc();

		shoot_gun(game);
	}

	private void updateLoc() {
		double s = getSpeed();
		double f = Main.frameRate;
		
		if (f < Main.MIN_FPS)
			f = Main.MIN_FPS;
		
		s *= 60.0/f;
	
		if (controller.up)
			updateLocY(-s);
		if (controller.down)
			updateLocY(s);
		if (controller.left)
			updateLocX(-s);
		if (controller.right)
			updateLocX(s);
	}

	public void render(PGraphics p) {
		if (speedBoostTimeout > -1) {
			p.fill(51, 205, 255);
			p.rect(locX() - 25, locY() - getRadius() - 15, speedBoostTimeout / 5, 5);
		}
		p.stroke(0);
		p.fill(255, 0, 255);
		p.pushMatrix();
		p.translate(locX(), locY());
		PImage img = Sprites.get(controller.direction());
		p.image(img, -img.width/2, -img.height/2);
		// p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.popMatrix();
	}
}
