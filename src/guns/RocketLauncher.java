package guns;

import entities.bullets.Bullet;
import entities.bullets.PlayerRocket;
import entities.creatures.players.Player;
import general.Game;
import general.Main;
import general.Sound;
import processing.core.PVector;

public class RocketLauncher extends PlayerGun {
	public RocketLauncher() {
		super(3, 100);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			PVector dir = new PVector(Main.mouseX - Main.WIDTH / 2, Main.mouseY - Main.HEIGHT / 2);
			Bullet b = new PlayerRocket(dir, player.locCopy(), 6, player.getAngle(), 1);
			player.getBullets().add(b);
			Sound.playRocketFired();
			return true;
		}
		return false;
	}
}
