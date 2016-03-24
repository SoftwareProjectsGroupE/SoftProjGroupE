package entities.creatures.players;

import general.Camera;
import general.Game;
import general.Main;
import general.Sprites;
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
		if (controller.up)
			updateLocY(-getSpeed());
		if (controller.down)
			updateLocY(getSpeed());
		if (controller.left)
			updateLocX(-getSpeed());
		if (controller.right)
			updateLocX(getSpeed());
	}

	public void render(PGraphics p, PImage img) {
		if (speedBoostTimeout > -1) {
			p.fill(51, 205, 255);
			p.rect(locX() - 25, locY() - getRadius() - 15, speedBoostTimeout / 5, 5);
		}
		p.stroke(0);
		p.fill(255, 0, 255);
		p.pushMatrix();
		p.translate(locX(), locY());
		PImage img1 = Sprites.get(controller.direction());
		p.image(img1, -img1.width/2, -img1.height/2);
		// p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.popMatrix();
	}
}
