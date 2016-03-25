package entities.creatures.enemies;

import entities.bullets.Bullet;
import entities.creatures.players.Player;
import general.Game;
import general.GameSP;
import general.Main;
import general.Sprites;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class StandardEnemy extends Enemy {
	
	private final PImage img = Main.getInstance().loadImage("./res/images/sprites/enemies/test.bmp");
	
	public StandardEnemy(double health, PVector loc, int radius, double speed, double cd) {
		super(health, loc, radius, speed, cd);
	}
	
	@Override 
	public void update(Game game) {
		if(Math.random() < 0.02)
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
		Bullet b = new Bullet(locCopy(), 4, 4, a + spread, 0.05);
		getBullets().add(b);
	}

	@Override
	public void onPlayerCollision(GameSP game, Player player) {}
	@Override 
	public void onWallCollision() {}
}
