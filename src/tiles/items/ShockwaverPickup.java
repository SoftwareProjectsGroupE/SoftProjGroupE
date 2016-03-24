package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerSP;
import guns.Shockwaver;
import guns.ShockwaverMP;

public class ShockwaverPickup extends ItemTile {

	public ShockwaverPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerSP) {
			player.addGun(new Shockwaver());
		} else {
			player.addGun(new ShockwaverMP());
		}
	}
}
