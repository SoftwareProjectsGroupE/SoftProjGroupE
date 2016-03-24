package general;

import java.util.ArrayList;
import java.util.List;

import entities.bullets.ExplosionAnimation;
import entities.creatures.players.Player;
import gui.Button;
import gui.Function;
import gui.Inventory;
import map.Map;
import multiplayer.GameMP;
import processing.core.PVector;
import states.State;

public abstract class Game implements State {

	private List<ExplosionAnimation> explosions = new ArrayList<ExplosionAnimation>();

	public final Button exitButton = new Button("Exit", Main.WIDTH - 75, 0, 75, 25);
	public final Button inventoryButton = new Button("Inventory", 0, Main.HEIGHT - 25, 100, 25);

	public final Inventory inventory = new Inventory();

	protected Player player;
	
	public abstract PVector screen_loc();
	public abstract Map get_map();

	public void mousePressed(int mouseX, int mouseY) {

		player.mousePressed(mouseX, mouseY);

		if (exitButton.mouseOver(mouseX, mouseY)) {
			exitButton.press(new Function() {});
		}

		if (inventoryButton.mouseOver(mouseX, mouseY)) {
			inventoryButton.press(new Function() {
				public void invoke() {
					inventory.switch_visible();
					player.temp_disable_mouse();
				}
			});
		}

		inventory.check_item_selected(player);
	}
	
	public Player getPlayer() {
		return player;
	}

	public GameSP asSP() {
		return (GameSP) this;
	}

	public GameMP asMP() {
		return (GameMP) this;
	}

	public void onStart() {
	}

	public void onExit() {
	}

	public void mouseDragged(int mouseX, int mouseY) {
	}

	public void add_explosion(ExplosionAnimation e) {
		explosions.add(e);
	}

	public List<ExplosionAnimation> get_explosions() {
		return explosions;
	}
}
