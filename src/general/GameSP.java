package general;

import java.util.List;

import entities.bullets.Bullet;
import entities.creatures.enemies.EnemyFactory;
import entities.creatures.players.PlayerSP;
import gui.Button;
import gui.Function;
import gui.Utils;
import map.Map;
import map.MapFactory;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import states.GameFinishedScreen;
import states.StateStack;

public class GameSP extends Game {
	
	private final Button pause_btn = new Button("Pause", 0, 0, 50, 25);

	private final MapFactory mapFactory = new MapFactory();
	private final EnemyFactory enemyFactory = new EnemyFactory();

	private Level level;
	protected int levelCount = 0;

	private Camera camera;

	public GameSP(Map map) {
		player = new PlayerSP(1, map.getPlayerSpawn(), 16, GameConstants.PLAYER_SPEED_SP);
		camera = new Camera(player.loc());
		level = new Level(this, map);
	}

	public GameSP() {
		this(MapFactory.firstLevel());
	}

	@Override
	public void update(Main m) {
		if (paused)
			return;
		
		level.update(m);
		
		if (!level.finished()) 
			return;
		
		Map next = mapFactory.nextLevel(get_map());
		if (next == null) {
			StateStack.setCurrentState(new GameFinishedScreen());
			return;
		}

		level = new Level(this, next);
		player.getBullets().clear();
		player.setLoc(next.getPlayerSpawn());
		camera.setAnchor(player.loc());
		
		Sound.playLevelFinished();
	}

	public void render(PGraphics p) {
		level.render(p);

		if (flashTime > 0) {
			float fade = (float) PApplet.map(flashTime, 0, flashTimeout, 0, 255);
			p.fill(255, fade);
			p.rect(0, 0, Main.WIDTH, Main.HEIGHT);
			flashTime--;
		}

		p.fill(0);
		//p.text("fps: " + (int) Main.frameRate, 100, 30);
		p.text("Level " + levelCount, 400, 12);
		//p.text("Map: " + get_map().name, 100, 80);

		exitButton.render(p, p.color(255, 0, 0));
		inventoryButton.render(p, p.color(255, 0, 0));
		pause_btn.render(p, p.color(255, 0, 0));

		inventory.render(p, player);
		
		if (level.enemies_cleared()) {
			p.fill(0, 255, 0, Utils.pulse(2.0));
			p.textSize(15);
			p.text("Get to the finish!", Main.WIDTH/2, Main.HEIGHT/2 - 60);
			p.textSize(12);
		}
	}
	
	private int flashTimeout = 0;
	private int flashTime = 0;

	public void setFlashTimeout(int f) {
		flashTimeout = f;
		flashTime = f;
	}
	
	private boolean paused = false;
	
	@Override
	public void mousePressed(int mouseX, int mouseY) {
		super.mousePressed(mouseX, mouseY);
		
		if (pause_btn.mouseOver(mouseX, mouseY)) {
			pause_btn.press(new Function() {
				public void invoke() {
					paused = !paused;
				}
			});
		}
	}

	public void keyPressed(int key) {
		player.keyPressed(key);
	}

	public void keyReleased(int key) {
		player.keyReleased(key);
	}

	/**
	 * Returns the pseudo position of the screen such that everything inside
	 * the screen is visible to the user.
	 */
	@Override
	public PVector screen_loc() {
		return new PVector(camera.locX() - Main.WIDTH / 2, camera.locY() - Main.HEIGHT / 2);
	}

	public int getLevelCount() {
		return levelCount;
	}

	public Level getLevel() {
		return level;
	}

	public Camera getCamera() {
		return camera;
	}

	public EnemyFactory getEnemyFactory() {
		return enemyFactory;
	}

	public List<Bullet> player_bullets() {
		return player.getBullets();
	}

	@Override
	public Map get_map() {
		return level.getMap();
	}
}
