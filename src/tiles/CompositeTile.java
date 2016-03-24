package tiles;

import entities.creatures.players.Player;
import processing.core.PGraphics;
import tiles.items.ItemTile;

public class CompositeTile extends Tile {

	public final Tile base;
	public final ItemTile item;
	
	public CompositeTile(Tile base, ItemTile item) {
		this.base = base;
		this.item = item;
	}
	
	@Override 
	public void render(PGraphics buffer, int x, int y) {
		base.render(buffer, x, y);
		item.render(buffer, x, y);
	}
	
	public void onPickup(Player player) {
		item.onPickup(player);
	}
}
