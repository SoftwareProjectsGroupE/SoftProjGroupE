package guns;

import entities.creatures.players.Player;
import general.Game;
import general.Sound;
import multiplayer.GameMP;
import processing.core.PApplet;

public class ShockwaverMP extends PlayerGun {
	public ShockwaverMP() {
		super(5, 500);
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			for (int i = 0; i < 360; i++) 
				((GameMP) game).send_bullet_fired(5, 20, PApplet.radians(i));
			Sound.playShockwave();
			return true;
		}
		return false;
	}
}
