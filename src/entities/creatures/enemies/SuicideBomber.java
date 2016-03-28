package entities.creatures.enemies;

import entities.bullets.ExplosionAnimation;
import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import general.Sound;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class SuicideBomber extends Enemy {

	public SuicideBomber(PImage img, double health, PVector loc, int radius, double speed, double cd) {
		super(img, health, loc, radius, speed, cd);
	}

	@Override
	public void update(Game game) {
		move(game.get_map(), game.getPlayer().locCopy());
	}

	@Override
	public void onPlayerCollision(GameSP game, Player p) {
		ExplosionAnimation e = new ExplosionAnimation(loc(), 50, 20);
		game.add_explosion(e);
		Sound.playSmallExplosion();
		this.onRemove(game);
	}

	@Override
	public void render(PGraphics p) {
		p.fill(255, 0, 0);
		p.pushMatrix();
		p.translate(locX(), locY());
		//p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.rotate((float) getAngle() + PApplet.radians(90));
		p.image(img, -img.width/2, -img.height/2);
		p.popMatrix();

	}

	@Override
	public void onWallCollision() {
		// TODO Auto-generated method stub

	}
}
