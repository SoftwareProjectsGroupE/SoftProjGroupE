package tiles;

import general.Main;
import map.Map;
import processing.core.PGraphics;
import processing.core.PImage;

public class Tile {
	
	private String name;
	private PImage img;
	private String id;
	public final boolean solid, walkable;
	// destroyable
	
	public Tile() {
		solid = false;
		walkable = true;
	}

	public Tile(String name, String file, boolean solid, boolean walkable) {
		PImage img = null;
		if(file != null) {
			img = Main.getInstance().loadImage("./res/images/tileset/" + file);
			img.resize(Map.BLOCK_SIZE, Map.BLOCK_SIZE);
		}
		this.img = img;
		this.name = name;
		this.solid = solid;
		this.walkable = walkable;
		int temp = TileFactory.tileCount();
		id = String.valueOf(temp);
		id = "000".substring(id.length()) + id;
	}
	
	public Tile(String name, boolean solid, boolean walkable) {
		this(name, null, solid, walkable);
	}
	
	public Tile(String name, String file) {
		this(name, file, false, true);
	}

	public void render(PGraphics buffer, int x, int y) {
		buffer.image(img, x << Map.LOG, y << Map.LOG);
	}
	
	public String get_name() {
		return name;
	}
	
	public String get_id() {
		return id;
	}
}
