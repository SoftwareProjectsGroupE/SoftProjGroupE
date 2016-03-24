package entities.creatures.enemies;

import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Crawler extends Enemy {
	
	public Crawler(double health, PVector loc, int radius, double speed, double collision_damage) {
		super(health, loc, radius, speed, collision_damage);
	}
	
	@Override public void update(Game game) {
		seek(game.getPlayer());
	}
	
	@Override public void render(PGraphics p, PImage img) {
		p.fill(255);
		p.pushMatrix();
		p.translate(locX(), locY());
		p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.rotate((float) getAngle());
		p.popMatrix();
	}
	
	@Override 
	public void onWallCollision() {}


	@Override
	public void onPlayerCollision(GameSP game, Player player) {}
}
