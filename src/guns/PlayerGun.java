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
		p.textSize(14);
		p.fill(0);
		p.text("Ammo", 100, 12);
		p.fill(50);
		p.rect(130, 10, 200, 10);
		p.fill(255);
		p.rect(130, 10, (float) (ammo / 5), 10);
	}

	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof PlayerGun))
			throw new IllegalArgumentException("arg should be a PlayerGun object");
		PlayerGun g = (PlayerGun) o;
		return g.id == this.id;
	}
}
