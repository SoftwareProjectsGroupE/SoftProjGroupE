package entities.bullets;

import general.Game;
import general.Sound;
import processing.core.PVector;

public class EnemyRocket extends Rocket {

	private final PVector target;

	public EnemyRocket(PVector target, PVector loc, double speed, double damge) {
		super(loc, speed, damge);
		this.target = target;
		Sound.playRocketFired();
	}

	@Override
	public void update(Game game) {
		PVector dir = PVector.sub(target, loc());
		dir.setMag((float) getSpeed());
		loc().add(dir);
		setAngle(dir.heading());
	}
}
