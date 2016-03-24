package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerSP;
import guns.Flamethrower;
import guns.FlamethrowerMP;

public class FlamethrowerPickup extends ItemTile {

	public FlamethrowerPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerSP) {
			player.addGun(new Flamethrower());
		} else {
			player.addGun(new FlamethrowerMP());
		}
	}
}
