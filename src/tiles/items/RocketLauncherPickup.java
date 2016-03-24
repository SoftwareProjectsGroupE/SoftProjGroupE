package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerMP;
import guns.RocketLauncher;

public class RocketLauncherPickup extends ItemTile {

	public RocketLauncherPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerMP)
			return;
		player.addGun(new RocketLauncher());
	}
}
