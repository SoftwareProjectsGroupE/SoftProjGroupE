package guns;

import entities.creatures.players.Player;
import general.Game;
import multiplayer.GameMP;

public class PistolMP extends PlayerGun {

	public PistolMP() {
		super(0, 0);
	}

	@Override
	public boolean outOfAmmo() {
		return false;
	}

	@Override
	public boolean shoot(Game game) {
		Player player = game.getPlayer();
		if (player.mouseClicked()) {
			((GameMP) game).send_bullet_fired(5, 10, player.getAngle());
			return true;
		}
		return false;
	}
}
