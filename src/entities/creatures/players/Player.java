package entities.creatures.players;

import java.util.ArrayList;
import java.util.List;

import entities.creatures.Creature;
import general.Controller;
import general.Game;
import guns.Pistol;
import guns.PlayerGun;
import processing.core.PVector;

public abstract class Player extends Creature {
	
	public final Controller controller = new Controller();

	List<PlayerGun> guns = new ArrayList<PlayerGun>();
	private int current_gun = 0;

	public Player(double health, PVector loc, int radius, double speed) {
		super(health, loc, radius, speed);
		ORIGINAL_SPEED = speed;
		guns.add(new Pistol());
	}

	public Player(double health, int radius, double speed) {
		super(health, radius, speed);
		ORIGINAL_SPEED = speed;
	}

	public final double ORIGINAL_SPEED;

	public int speedBoostTimeout = -1;

	public void startSpeedBoost() {
		speedBoostTimeout = 1000;
		setSpeed(2 * ORIGINAL_SPEED);
	}

	public void shoot_gun(Game game) {
		if (mouse_disabled_time > 0) {
			mouseClicked = false;
			--mouse_disabled_time;
			return;
		}

		PlayerGun gun = guns.get(current_gun);
		if (gun.shoot(game)) {
			gun.decreaseAmmo();
		}
		if (gun.outOfAmmo()) {
			guns.remove(current_gun);
			current_gun = guns.size() - 1;
		}
	}

	public void set_current_gun(int i) {
		current_gun = i;
	}

	public void addGun(PlayerGun gun) {
		if (!guns.contains(gun)) {
			guns.add(gun);
			current_gun = guns.size() - 1;
			return;
		}
		for (int i = 0; i < guns.size(); i++) {
			if (gun.equals(guns.get(i))) {
				guns.set(i, gun);
				current_gun = i;
				return;
			}
		}
	}
	
	private boolean mouseClicked = false;

	public boolean mouseClicked() {
		boolean b = mouseClicked;
		mouseClicked = false;
		return b;
	}

	public void mousePressed(int mouseX, int mouseY) {
		mouseClicked = true;
	}
	
	int mouse_disabled_time = 0;

	public void temp_disable_mouse() {
		mouse_disabled_time = 10;
	}
	
	public PlayerGun getGun() {
		return guns.get(current_gun);
	}

	public List<PlayerGun> getGuns() {
		return guns;
	}
	
	public PlayerMP asMP() {
		return (PlayerMP) this;
	}
	
	public void keyPressed(int key) {
		controller.keyPressed(key);
	}

	public void keyReleased(int key) {
		controller.keyReleased(key);
	}
}
