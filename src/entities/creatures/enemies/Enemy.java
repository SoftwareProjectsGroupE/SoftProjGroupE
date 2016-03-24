package entities.creatures.enemies;

import java.util.List;

import entities.creatures.Creature;
import entities.creatures.players.Player;
import general.GameConstants;
import general.GameSP;
import general.Main;
import general.Node;
import general.PathFinder;
import map.Map;
import processing.core.PVector;

public abstract class Enemy extends Creature {

	public final double COLLISION_DAMAGE;

	private PathFinder pathFinder = new PathFinder(true);

	public Enemy(double health, PVector loc, int radius, double speed, double collision_damage) {
		super(health, loc, radius, speed);
		COLLISION_DAMAGE = collision_damage;
	}

	public abstract void onPlayerCollision(GameSP game, Player player);

	public abstract void onWallCollision();

	public void seek(Creature target) {
		PVector dir = PVector.sub(target.loc(), loc());
		dir.setMag((float) getSpeed());
		loc().add(dir);
	}

	private List<Node> path = null;

	public void move(Map map, PVector playerLoc) {
		int bs = Map.BLOCK_SIZE;

		PVector start = locCopy().div(bs);

		PVector temp = null;
		if (path != null && path.size() > 0)
			temp = path.get(path.size() - 1).loc();

		// how often the path is recalculated
		if (Main.frameCount % GameConstants.A_STAR_INTERVAL == 0 || 
		   (temp != null && PVector.dist(start, temp) < getSpeed() + 1)) {
			PVector destination = playerLoc.div(bs);
			path = pathFinder.a_star_search(map, start, destination);
		}

		if (path != null && path.size() > 0 && temp != null) {
			PVector target = new PVector(temp.x * bs + bs / 2, temp.y * bs + bs / 2);
			PVector dir = PVector.sub(target, loc());
			loc().add(dir.setMag((float) getSpeed()));
		}

		// older logic without "next-point-reached" checking

		/*
		 * PVector start = getLocCopy().div(bs);
		 * 
		 * // how often the path is recalculated if(Main.frameCount % 15 == 0) {
		 * 
		 * PVector destination = player.getLocCopy().div(bs); path =
		 * pathFinder.aStarSearch(start, destination); }
		 * 
		 * if(path != null && path.size() > 0) { PVector temp =
		 * path.get(path.size() - 1).loc(); PVector target = new PVector(temp.x
		 * * bs + bs/2, temp.y * bs + bs/2); PVector dir = PVector.sub(target,
		 * getLoc()); getLoc().add(dir.setMag((float) SPEED)); }
		 */
	}
}
