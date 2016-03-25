package entities.creatures.enemies;

import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Crawler extends Enemy {
	
	public Crawler(PImage img, double health, PVector loc, int radius, double speed, double collision_damage) {
		super(img, health, loc, radius, speed, collision_damage);
	}
	
	@Override public void update(Game game) {
		seek(game.getPlayer());
	}
	
	@Override public void render(PGraphics p) {
		p.fill(255);
		p.pushMatrix();
		p.translate(locX(), locY());
		//p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.rotate((float) getAngle() + PApplet.radians(90));
		p.image(img, -img.width/2, -img.height/2);
		p.popMatrix();
	}
	
	@Override 
	public void onWallCollision() {}


	@Override
	public void onPlayerCollision(GameSP game, Player player) {}
}
