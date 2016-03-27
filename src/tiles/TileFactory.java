package tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import tiles.items.FlamethrowerPickup;
import tiles.items.HealthPack;
import tiles.items.ItemTile;
import tiles.items.LaserPickup;
import tiles.items.RocketLauncherPickup;
import tiles.items.ShockwaverPickup;
import tiles.items.ShotgunPickup;
import tiles.items.SpeedBoost;
import tiles.items.UziPickup;

public class TileFactory {
	
	private static final List<Tile> tiles = new ArrayList<Tile>();
	
	public static void loadTiles() {	
		tiles.add(new Tile("Spawn-Area", "spawn.png", false, true));
		tiles.add(new Tile("Finish-Area", "finish.jpg", false, true));
		tiles.add(new Tile("Grass", "grass.jpg", false, true));
		tiles.add(new Tile("Ground 1", "ground.jpg", false, true));
		tiles.add(new Tile("Ground 2", "ground2.jpg", false, true));
		tiles.add(new Tile("Ground 3", "ground3.jpg", false, true));
		tiles.add(new Tile("Wall", "wall.jpg", true, false));		
		tiles.add(new Tile("Wood 1", "wood.jpg", false, true));		
		tiles.add(new Tile("Wood 2", "wood2.jpg", false, true));
		tiles.add(new Tile("Wood 3", "wood3.jpg", false, true));
		tiles.add(new AnimatedTile("Water", "water/", ".bmp", 8, false, false));
		
		// items
		tiles.add(new ShotgunPickup("Shotgun", "shotty.png"));
		tiles.add(new UziPickup("Uzi", "uzi.png"));		
		tiles.add(new RocketLauncherPickup("Rocket Launcher", "rocket-launcher.png"));		
		tiles.add(new FlamethrowerPickup("Flamethrower", "flamethrower.png"));
		tiles.add(new ShockwaverPickup("Shockwaver", "shockwaver.png"));
		tiles.add(new LaserPickup("Laser", "laser.png"));
		tiles.add(new HealthPack("Health pack", "health.png"));
		tiles.add(new SpeedBoost("Speed boost", "speed.png"));
		
		load_bulk();
	}
	
	private static void load_bulk() {
		File walkable = new File("./res/images/tileset/walkable/");
		File[] files = walkable.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			// i put this file in the wrong folder. too late to change now as maps use it 
			if (file.getName().equals("000135.jpg")) {
				tiles.add(new Tile("", "walkable/" + file.getName(), true, false));
			} else {
				tiles.add(new Tile("", "walkable/" + file.getName(), false, true));
			}
		}
		File solid = new File("./res/images/tileset/solid/");
		File[] files2 = solid.listFiles();
		Arrays.sort(files2);
		for (File file : files2)
			tiles.add(new Tile("", "solid/" + file.getName(), true, false));
	}
	
	public static int tileCount() {
		return tiles.size();
	}
	
	public static boolean isSpawn(Tile tile) {
		return tile != null && "Spawn-Area".equals(tile.get_name());
	}
	
	public static boolean isFinish(Tile tile) {
		return tile != null && "Finish-Area".equals(tile.get_name());
	}

	public static Tile getTile(String type) {
		if(type == null) 
			return null;
		
		if(type.equals("null")) 
			return null;
		
		if(type.length() == 6) {
			String id1 = type.substring(0, 3);
			String id2 = type.substring(3, 6);
			Tile base = tiles.get(Integer.parseInt(id1));
			ItemTile itemTile = (ItemTile) tiles.get(Integer.parseInt(id2));
			return new CompositeTile(base, itemTile);
		}
		
		int index = Integer.parseInt(type);
		if(index < 0 || index >= tiles.size())
			throw new IllegalArgumentException("This tile doesn't exist: " + type);
		return tiles.get(index);
	}
}








