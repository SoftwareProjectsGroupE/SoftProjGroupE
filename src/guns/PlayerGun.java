package guns;

import general.Game;
import general.GameConstants;
import processing.core.PGraphics;

public abstract class PlayerGun {
	
	public final int id;
	private int ammo = GameConstants.DEFAULT_AMMO;
	private final int ammo_dcrmnt;

	public PlayerGun(int id, int ammo_dcrmnt) {
		this.id = id;
		this.ammo_dcrmnt = ammo_dcrmnt;
	}

	public abstract boolean shoot(Game game);

	public void decreaseAmmo() {
		ammo -= ammo_dcrmnt;
		if (ammo < 0)
			ammo = 0;
	}

	public boolean outOfAmmo() {
		return ammo <= 0;
	}

	public void render(PGraphics p) {
		p.fill(50);
		p.rect(200, 5, 200, 5);
		p.fill(255);
		p.rect(200, 5, (float) (ammo / 5), 5);
	}

	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof PlayerGun))
			throw new IllegalArgumentException("arg should be a PlayerGun object");
		PlayerGun g = (PlayerGun) o;
		return g.id == this.id;
	}
}
