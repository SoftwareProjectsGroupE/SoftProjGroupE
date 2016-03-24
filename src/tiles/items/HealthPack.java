package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerMP;

public class HealthPack extends ItemTile {

	public HealthPack(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerMP)
			return;
		player.updateHealth(0.3);
	}
}
