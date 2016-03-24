package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerMP;
import guns.PlayerLaser;

public class LaserPickup extends ItemTile {

	public LaserPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerMP)
			return;
		player.addGun(new PlayerLaser());
	}
}
