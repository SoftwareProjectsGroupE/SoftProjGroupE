package entities;

import general.Game;
import general.Main;
import general.Removeable;
import processing.core.PGraphics;
import processing.core.PVector;

public abstract class Entity implements Removeable {
		
	private PVector loc;
	private int radius;
	
	private boolean removed = false;

	public Entity(PVector loc, int radius) {
		this.loc = loc;
		this.radius = radius;
	}

	public abstract void render(PGraphics p);
	
	public double angleTo(Entity target) {
		return Math.atan2(target.loc.y - loc.y, target.loc.x - loc.x);
	}

	public boolean collides(Entity e) {
		return loc.dist(e.loc) < radius + e.radius;
	}
	
	public void onRemove(Game game) {
		removed = true;
	}
	
	@Override
	public boolean removed() {
		return removed;
	}
	
	public boolean offScreen(float screenX, float screenY, int offset) {
		return loc.x + radius < screenX - offset || 
			   loc.x - radius > screenX + Main.WIDTH + offset|| 
			   loc.y + radius < screenY - offset || 
			   loc.y - radius > screenY + Main.HEIGHT + offset;
	}
	
	public boolean offScreen(float screenX, float screenY) {
		return offScreen(screenX, screenY, 0);
	}
	
	public boolean onScreen(float screenX, float screenY) {
		return !offScreen(screenX, screenY, 0);
	}

	public boolean onScreen(float screenX, float screenY, int offset) {
		return !offScreen(screenX, screenY, offset);
	}
	
	public boolean onScreen(PVector screenLoc) {
		return onScreen(screenLoc.x, screenLoc.y, 0);
	}
	
	public boolean onScreen(PVector screenLoc, int offset) {
		return onScreen(screenLoc.x, screenLoc.y, offset);
	}
	
	public boolean offScreen(PVector screenLoc) {
		return !onScreen(screenLoc);
	}
	
	public boolean offScreen(PVector screenLoc, int offset) {
		return !onScreen(screenLoc, offset);
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int r) {
		radius = r;
	}
	
	public int diameter() {
		return 2 * radius;
	}

	public PVector loc() {
		return loc;
	}
	
	public void setLoc(PVector p) {
		loc = p.copy();
	}
	
	public PVector locCopy() {
		return loc.copy();
	}

	public float locX() {
		return loc.x;
	}
	
	public float locY() {
		return loc.y;
	}
	
	public void updateLocX(float x) {
		loc.x += x;
	}
	
	public void updateLocY(float y) {
		loc.y += y;
	}
	
	public void updateLocX(double x) {
		loc.x += x;
	}
	
	public void updateLocY(double y) {
		loc.y += y;
	}
	
	public void setLocX(double x) {
		loc.x = (float) x;
	}
	
	public void setLocY(double y) {
		loc.y = (float) y;
	}
	
	public void setLocX(float x) {
		loc.x = x;
	}
	
	public void setLocY(float y) {
		loc.y = y;
	}
}