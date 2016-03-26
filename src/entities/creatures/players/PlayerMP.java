package entities.creatures.players;



import java.util.ArrayList;

import general.Game;
import general.Main;
import general.Sprites;
import guns.PistolMP;
import guns.PlayerGun;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class PlayerMP extends Player {
	
	private boolean moved = false;

	public PlayerMP(double health, int radius, double speed) {
		super(health, radius, speed);
		guns.add(new PistolMP());
	}
	
	public void reset() {
		controller.reset();
		speedBoostTimeout = -1;
		setSpeed(ORIGINAL_SPEED);
		guns = new ArrayList<PlayerGun>();
		guns.add(new PistolMP());
		set_current_gun(0);
		resetHealth();
	}

	@Override
	public void update(Game game) {
		double a = PApplet.atan2(Main.mouseY - Main.HEIGHT/2, Main.mouseX - Main.WIDTH/2);
		setAngle(a);
		
		handleMoving();
		
		if(speedBoostTimeout > -1) 
			if(--speedBoostTimeout == 0) 
				setSpeed(ORIGINAL_SPEED);
		
		shoot_gun(game);
	}

	private void handleMoving() {
		moved = false;
		
		double ds = getSpeed();
		double f = Main.frameRate;
		
		if(f < Main.MIN_FPS)
			f = Main.MIN_FPS;
		
		ds *= 60.0/f;
		
		int s = (int) ds;
		
		if (controller.right) 
			updateLocX(s);
		if (controller.left)  
			updateLocX(-s);
		if (controller.down)  
			updateLocY(s);
		if (controller.up)    
			updateLocY(-s);

		if (controller.up || controller.down || controller.left || controller.right) {
			moved = true;
		}
	}
	
	public boolean moved() {
		return moved;
	}

	public void render(PGraphics p) {
		if(speedBoostTimeout > -1) {
			p.fill(51, 205, 255);
			p.rect(Main.WIDTH/2 - 25, Main.HEIGHT/2 -getRadius() - 15, speedBoostTimeout/5, 5);
		}
		//p.fill(255);
		//p.ellipse(Main.WIDTH/2, Main.HEIGHT/2, 30, 30);
		
		
		

		PImage img = Sprites.get(controller.direction());
		p.image(img, Main.WIDTH/2 - img.width/2, Main.HEIGHT/2 - img.height/2);
	}

	private boolean respawned = false;

	public void setRespawned(boolean b) {
		respawned = b;
	}
	
	public boolean respawned() {
		return respawned;
	}
}
