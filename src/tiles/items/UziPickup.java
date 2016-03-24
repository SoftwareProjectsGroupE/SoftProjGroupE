package tiles.items;

import entities.creatures.players.Player;
import entities.creatures.players.PlayerSP;
import guns.Uzi;
import guns.UziMP;

public class UziPickup extends ItemTile {

	public UziPickup(String name, String file) {
		super(name, file);
	}

	@Override
	public void onPickup(Player player) {
		if (player instanceof PlayerSP) {
			player.addGun(new Uzi());
		} else {
			player.addGun(new UziMP());
		}
	}
}
