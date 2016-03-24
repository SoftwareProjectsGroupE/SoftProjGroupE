package tiles.items;

import entities.creatures.players.Player;
import tiles.Tile;

public abstract class ItemTile extends Tile {
	
	public ItemTile(String name, String file) {
		super(name, file);
	}

	public abstract void onPickup(Player player);
}
