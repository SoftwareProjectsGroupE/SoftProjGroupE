package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerSP;
import guns.Shotgun;
import guns.ShotgunMP;

public class ShotgunPickup extends ItemTile {

	public ShotgunPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerSP) {
			player.addGun(new Shotgun());
		} else {
			player.addGun(new ShotgunMP());
		}
	}
}
