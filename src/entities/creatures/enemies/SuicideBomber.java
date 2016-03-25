package entities.creatures.enemies;

import entities.bullets.ExplosionAnimation;
import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class SuicideBomber extends Enemy {

	public SuicideBomber(double health, PVector loc, int radius, double speed, double cd) {
		super(health, loc, radius, speed, cd);
	}

	@Override
	public void update(Game game) {
		move(game.get_map(), game.getPlayer().locCopy());
	}

	@Override
	public void onPlayerCollision(GameSP game, Player p) {
		ExplosionAnimation e = new ExplosionAnimation(loc(), 50, 20);
		game.add_explosion(e);
		this.onRemove(game);
	}

	@Override
	public void render(PGraphics p) {
		p.fill(255, 0, 0);
		p.pushMatrix();
		p.translate(locX(), locY());
		p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.rotate((float) getAngle());
		p.popMatrix();

	}

	@Override
	public void onWallCollision() {
		// TODO Auto-generated method stub

	}
}
