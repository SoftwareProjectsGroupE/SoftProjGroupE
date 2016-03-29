package general;

import java.util.ArrayList;
import java.util.List;

import entities.bullets.Bullet;
import entities.bullets.ExplosionAnimation;
import entities.creatures.enemies.Enemy;
import entities.creatures.enemies.EnemyFactory;
import entities.creatures.players.Player;
import gui.Utils;
import map.Map;
import processing.core.PGraphics;
import processing.core.PVector;
import states.GameOverScreen;
import states.StateStack;
import tiles.CompositeTile;

public class Level {

	private List<Enemy> enemies = new ArrayList<Enemy>();

	private final GameSP game;
	private final Map map;

	public Level(GameSP game, Map map) {
		this.game = game;
		this.map = map;
		if (isBossLevel()) {
			PVector bs = map.getBossSpawn().copy();
			Enemy boss = game.getEnemyFactory().nextBoss(bs, game.levelCount);
			enemies.add(boss);
			Sound.playBossLaugh();
		} else
			spawnEnemies();
		Sound.playTheme(game.levelCount);
	}

	private boolean isBossLevel() {
		return ++game.levelCount % 3 == 0 || game.levelCount == 10;
	}

	private void spawnEnemies() {
		
		List<PVector> spawnPoints = map.getSpawnPoints();
		EnemyFactory enemyFactory = game.getEnemyFactory();
		PathFinder path_finder = new PathFinder(false);
		
		PVector ps = map.getPlayerSpawn();
		PVector unit_player_spawn = new PVector(ps.x/Map.BLOCK_SIZE, ps.y/Map.BLOCK_SIZE);

		// change loop increment to gauge enemy density
		for (int i = 0; i < spawnPoints.size(); i += 100) {
			PVector p = spawnPoints.get(i).copy();
			PVector unit_spawn_point = new PVector(p.x/Map.BLOCK_SIZE, p.y/Map.BLOCK_SIZE);
			
			// makes sure enemies don't spawn trapped in closed rooms
			if (path_finder.a_star_search(map, unit_spawn_point, unit_player_spawn) == null)
				continue;

			Enemy e = enemyFactory.randomEnemy(p, game.levelCount);
			
			if (e.offScreen(ps.x - Main.WIDTH/2, ps.y - Main.HEIGHT/2))
				enemies.add(e);
		}
	}

	public void update(Main m) {
		updateEnemies();
		updateEnemyBullets();
		updatePlayer();
		updatePlayerBullets();

		for (ExplosionAnimation e : game.get_explosions())
			e.update();

		if (map.isFinish(game.getPlayer().loc()))
		    if(enemies_cleared()/* || GameConstants.DEBUG_MODE*/) 
			    finished = true;

		List<Bullet> playerBullets = game.getPlayer().getBullets();
		collect_garbage(playerBullets);

		for (Enemy e : enemies) 
			collect_garbage(e.getBullets());
		
		collect_garbage(enemies);

		collect_garbage(game.get_explosions());
	}
	
	private void collect_garbage(List<? extends Removeable> garbage) {
		for (int i = garbage.size() - 1; i >= 0; i--)
			if (garbage.get(i).removed())
				garbage.remove(i);
	}
	
	public boolean enemies_cleared() {
		return enemies.size() == 0;
	}

	private boolean finished = false;

	public boolean finished() {
		return finished;
	}

	void updatePlayer() {
		Player player = game.getPlayer();
		player.update(game);
		player.seperate(enemies);
		//if(!GameConstants.DEBUG_MODE)
			player.collidedWall(map);

		CompositeTile tile = map.getItem(player.loc());
		if (tile != null) {
			tile.onPickup(player);
			map.setTile(player.loc(), tile.base);
			Sound.playPickupSound();
		}

		if (player.dead() && !GameConstants.DEBUG_MODE) {
			Sound.playPlayerDeath();
			StateStack.setCurrentState(new GameOverScreen());
		}
	}

	private List<Enemy> newEnemies = new ArrayList<Enemy>();

	public void addEnemy(Enemy e) {
		newEnemies.add(e);
	}

	private void updateEnemies() {
		enemies.addAll(newEnemies);
		newEnemies.clear();

		for (Enemy e : enemies) {
			if (e.offScreen(game.screen_loc(), 200))
				continue;

			e.update(game);
			e.seperate(enemies);

			if (e.collidedWall(map))
				e.onWallCollision();

			Player player = game.getPlayer();
			if (e.collides(player)) {
				e.onPlayerCollision(game, player);
				player.updateHealth(e.COLLISION_DAMAGE);
			}

			for (Bullet b : player.getBullets()) {
				if (e.collides(b)) {
					e.updateHealth(-b.DAMAGE);
					b.onRemove(game);
				}
			}

			if (e.dead()) {
				e.onRemove(game);
				Sound.playEnemyDeath();
			}
		}
	}

	private void updatePlayerBullets() {
		for (Bullet b : game.player_bullets()) {
			b.update(game);

			if (b.offScreen(game.screen_loc()))
				b.onRemove(game);

			else if (map.solid(b.loc()))
				b.onRemove(game);
		}
	}

	private void updateEnemyBullets() {
		Player player = game.getPlayer();
		
		for (Enemy e : enemies) {
			for (Bullet b : e.getBullets()) {
				b.update(game);
				
				if (map.solid(b.loc())) {
					b.onRemove(game);
					continue;
				}
				
				if (b.collides(player)) {
					b.onRemove(game);
					player.updateHealth(-b.DAMAGE);
					continue;
				}
	
				for (Bullet pb : player.getBullets()) {
					if (b.collides(pb)) {
						pb.onRemove(game);
						b.onRemove(game);
					}
				}
			}
		}
	}

	public void render(PGraphics p) {
		Camera camera = game.getCamera();
		Player player = game.getPlayer();
		
		PVector scrn_loc = game.screen_loc();

		p.pushMatrix();
		camera.update(p);
		p.background(0);
		map.render(p, camera.locX(), camera.locY());

		for (Bullet b : player.getBullets())
			if (b.onScreen(scrn_loc))
				b.render(p);

		for (Enemy e : enemies) {
			for (Bullet b : e.getBullets())
				if (b.onScreen(scrn_loc))
					b.render(p);
			if (e.onScreen(scrn_loc, 50))
				e.render(p);
		}
		
		if (enemies.size() < 5) {
			for (Enemy e : enemies) {
				if (e.offScreen(scrn_loc)) {					
					p.stroke(255, 0, 0, Utils.pulse(2.0));
					p.line(e.locX(), e.locY(), player.locX(), player.locY());
				}
			}
		}
		
		if (enemies_cleared()) {
			PVector fin = map.getBossSpawn();
			p.stroke(0, 255, 0, Utils.pulse(2.0));
			p.line(fin.x, fin.y, player.locX(), player.locY());
		}
		
		for (ExplosionAnimation e : game.get_explosions())
			e.render(p);

		player.render(p);
		p.popMatrix();
		
		if (!(game.levelCount % 3 == 0 || game.levelCount == 10)) {
			if (enemies.size() < 5 && !enemies.isEmpty()) {
				p.fill(255, 0, 0, Utils.pulse(2.0));
				p.textSize(15);
				p.text("Finish off the enemies!", Main.WIDTH/2, Main.HEIGHT/2 - 60);
				p.textSize(12);
			}
		}

		player.getGun().render(p);

		p.fill(0);
		p.text("enemies left: " + enemies.size(), 600, 12);
	}

	public Map getMap() {
		return map;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}
}
