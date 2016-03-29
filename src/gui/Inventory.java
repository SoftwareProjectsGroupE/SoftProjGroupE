package gui;

import java.util.List;

import entities.creatures.players.Player;
import general.Main;
import guns.PlayerGun;
import processing.core.PGraphics;
import processing.core.PImage;

public class Inventory {

	private static final PImage[] images = new PImage[7];
	
	private static final int BTN_SIZE = 100;

	public static void load_images() {
		add_img(0, "pistol.png");
		add_img(1, "shotty.png");
		add_img(2, "uzi.png");
		add_img(3, "rocket-launcher.png");
		add_img(4, "flamethrower.png");
		add_img(5, "shockwaver.png");
		add_img(6, "laser.png");
	}

	private boolean visible = false;

	public void switch_visible() {
		visible = !visible;
	}

	public void check_item_selected(Player player) {
		if (visible) {
			for (int i = 0; i < player.getGuns().size(); i++) {
				if (mouse_over_item(i)) {
					player.set_current_gun(i);
					visible = false;
					player.temp_disable_mouse();
					return;
				}
			}
		}
	}

	private boolean mouse_over_item(int i) {
		return Main.mouseX > (i + 1) * BTN_SIZE && Main.mouseX < (i + 2) * BTN_SIZE && Main.mouseY > Main.HEIGHT - BTN_SIZE;
	}

	public void render(PGraphics p, Player player) {
		if (visible) {
			List<PlayerGun> guns = player.getGuns();
			for (int i = 0; i < guns.size(); i++) {
				PlayerGun gun = guns.get(i);
				p.fill(mouse_over_item(i) ? 255 : 200);
				p.rect(BTN_SIZE + i * BTN_SIZE, Main.HEIGHT - BTN_SIZE, BTN_SIZE, BTN_SIZE);
				p.image(get_img(gun.id), BTN_SIZE + i * BTN_SIZE, Main.HEIGHT - BTN_SIZE);
			}
		}
		int id = player.getGun().id;
		p.image(images[id], 10, Main.HEIGHT - 100, 70, 50);
	}

	private static void add_img(int id, String file) {
		PImage img = Main.getInstance().loadImage("./res/images/tileset/" + file);
		img.resize(BTN_SIZE, BTN_SIZE);
		images[id] = img;
	}

	private static PImage get_img(int id) {
		if (id < 0 || id >= images.length)
			throw new IllegalArgumentException("This image doesn't exist: " + id);
		return images[id];
	}
}
