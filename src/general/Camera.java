package general;

import processing.core.PGraphics;
import processing.core.PVector;

public class Camera {

	private PVector anchor;
	private PVector loc;

	public Camera(PVector p) {
		anchor = p;
		loc = p.copy();
	}

	public void update(PGraphics p) {
		if (loc.dist(anchor) > 2) {
			PVector dir = PVector.sub(anchor, loc);
			dir.div(GameConstants.CAMERA_EASING);
			loc.add(dir);
		}

		p.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
		p.translate(-loc.x, -loc.y);

		if (rumble > 0) {
			p.translate((float) Math.random() * rumble, (float) Math.random() * rumble);
			rumble--;
		}
	}

	private int rumble = 0;

	public void setRumble(int r) {
		rumble = r;
	}

	public void setAnchor(PVector anchor) {
		this.anchor = anchor;
	}

	public PVector loc() {
		return loc;
	}

	public float locX() {
		return loc.x;
	}

	public float locY() {
		return loc.y;
	}
}
