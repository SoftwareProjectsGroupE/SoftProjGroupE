package entities.creatures;


import java.util.ArrayList;
import java.util.List;

import entities.MobileEntity;
import entities.bullets.Bullet;
import map.Map;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public abstract class Creature extends MobileEntity {
	
	private List<Bullet> bullets = new ArrayList<Bullet>();
	
	private double health;
	private final double start_health;
	
	private boolean pushable = true;

	public Creature(double health, PVector loc, int radius, double speed) {
		super(loc, radius, 0.0, speed);
		this.health = health;
		start_health = health;
	}
	
	public Creature(double health, int radius, double speed) {
		this(health, new PVector(), radius, speed);
	}
	
	/**
	 * Seperates this Creature from every other creature in the given list.
	 * */
	public boolean seperate(Iterable<? extends Creature> others) {
		boolean collisionHappened = false;
		for (Creature other : others) 
			if (seperate(other)) 
				collisionHappened = true;
		return collisionHappened;
	}
	
	public boolean seperate(Creature other) {
		boolean collisionHappened = false;
		// Don't seperate from yourself!
		if (other != this) {
			if (other.collides(this)) {
				// Push both creatures directly away from each other.
				PVector between = PVector.sub(this.loc(), other.loc());
				loc().add(between.setMag((float) getSpeed()));
				if (other.pushable)
					other.loc().sub(between.setMag((float) other.getSpeed()));
				collisionHappened = true;
			}
		}
		return collisionHappened;
	}

	public boolean collidedWall(Map map) {
		int bs = Map.BLOCK_SIZE;;
		int ux = (int) locX()/bs;
		int uy = (int) locY()/bs;
		int offset = getRadius();
		
		if (offset > bs/2)
			offset = bs/2;
		
		boolean collisionHappened = false;
		if (!map.walkable(ux, uy + 1)) {
			if (loc().y > (uy + 1) * bs - offset) {
				loc().y = (uy + 1) * bs - offset;
				collisionHappened = true;
			}
		}
		if (!map.walkable(ux + 1, uy)) { 
			if (loc().x > (ux + 1) * bs - offset) {
				loc().x = (ux + 1) * bs - offset;
				collisionHappened = true;
			}
		}
		if (!map.walkable(ux, uy - 1)) { 
			if (loc().y < uy * bs + offset) {
				loc().y = uy * bs + offset;
				collisionHappened = true;
			}	
		}
		if (!map.walkable(ux - 1, uy)) { 
			if (loc().x < ux * bs + offset) { 
				loc().x = ux * bs + offset;
				collisionHappened = true;
			}
		}
		return collisionHappened;
	}
	
	public void renderHealthBar(PGraphics p, int width) {
		p.pushMatrix();
		p.translate(0, 0, 1);
		p.fill(255, 0, 0);
		p.rect(-width/2, -getRadius() - 10, width, 5);
		p.fill(0, 255, 0);
		float w = PApplet.map((float) health, 0, (float) start_health, 0, width);
		if(w < 0) w = 0;
		p.rect(-width/2, -getRadius() - 10, w, 5);
		p.popMatrix();
	}
	
	public void setPushable(boolean b) {
		pushable = b;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}
	
	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	public void updateHealth(double increment) {
		health += increment;
		if(health > start_health)
			health = start_health;
	}
	
	public void resetHealth() {
		health = 1;
	}
	
	public double getHealth() {
		return health;
	}

	public boolean dead() {
		return health <= 0;
	}
}
