package tiles.items;

import entities.creatures.players.Player;

public class SpeedBoost extends ItemTile {

	public SpeedBoost(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		player.startSpeedBoost();
	}
}
