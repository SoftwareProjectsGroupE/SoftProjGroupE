package entities.creatures.enemies;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import general.Main;
import general.Sound;
import general.Sprites;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class StandardEnemy extends Enemy {
	
	public StandardEnemy(PImage img, double health, PVector loc, int radius, double speed, double cd) {
		super(img, health, loc, radius, speed, cd);
	}
	
	@Override 
	public void update(Game game) {
		if(Math.random() < 0.01)
			shoot((GameSP) game);
		move(game.get_map(), game.getPlayer().locCopy());
	}
	
	@Override 
	public void render(PGraphics p) {
		p.fill(255);
		p.pushMatrix();
		p.translate(locX(), locY());
		//p.ellipse(0, 0, diameter(), diameter());
		renderHealthBar(p, 50);
		p.rotate((float) getAngle() + PApplet.radians(90));
		p.image(img, -img.width/2, -img.height/2);
		p.popMatrix();
	}
	
	public void shoot(GameSP game) {
		Player player = game.getPlayer();
		double a = angleTo(player);
		double spread = Math.random() * 0.3 - 0.15;
		Bullet b = new Bullet(locCopy(), 4, 3, a + spread, 0.05);
		getBullets().add(b);
		if (onScreen(game.screen_loc())) 
			Sound.playEnemyBullet();
	}

	@Override
	public void onPlayerCollision(GameSP game, Player player) {}
	@Override 
	public void onWallCollision() {}
}
