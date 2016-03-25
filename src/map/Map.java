package map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import general.Main;
import general.Node;
import processing.core.PGraphics;
import processing.core.PVector;
import tiles.CompositeTile;
import tiles.Tile;
import tiles.TileFactory;

public class Map //implements Iterator<int[]> 
{
	// must be multiple of 2
	public static final int BLOCK_SIZE = 32;
	public static final int LOG;

	static {
		int n = 2;
		int i = 1;
		while (n != BLOCK_SIZE) {
			if (n > BLOCK_SIZE)
				throw new RuntimeException("BLOCK_SIZE must be a multiple of 2");
			n *= 2;
			i++;
		}
		LOG = i;
	}

	public static final int GRID_SIZE = 150;
	public static final int MAP_SIZE = BLOCK_SIZE * GRID_SIZE;

	private final Tile[][] grid = new Tile[GRID_SIZE][GRID_SIZE];

	public final String path;
	public final String name;
	
	class Cell {
		int x;
		int y;
		Cell(int _x, int _y) {
			x = _x;
			y = _y;
		}
	}
	
	/**
	 * Returns every x,y coordinate on the map in grid unit precision.
	 *  
	 * */
	Cell[] cells() {
		Cell[] cells = new Cell[GRID_SIZE * GRID_SIZE];
		
		int L = GRID_SIZE;
		for (int i = 0; i < cells.length; i++) 
			cells[i] = new Cell(i%L, i/L);
		
		return cells;
	}

	public Map(String path) {
		this.path = path;
		
		String[] map_data;
		
		if (path == null) {
			
			map_data = new String[GRID_SIZE * GRID_SIZE];
			name = "blank";
			
		} else {
			
			map_data = Main.getInstance().loadStrings(path);
			name = path.substring(path.contains("dev") ? 18 : 21, path.length() - 4);
			
		}
		
		int L = GRID_SIZE;
		for (int i = 0; i < map_data.length; i++) 
			grid[i/L][i%L] = TileFactory.getTile(map_data[i]);
		//grid[i%L][i/L] = TileFactory.getTile(map_data[i]);
		
		initSpawnPoints();
	}

	public Map() {
		this(null);
	}

	private PVector playerSpawn = new PVector(0, 0);
	private PVector bossSpawn = new PVector(0, 0);
	private final ArrayList<PVector> spawnPoints = new ArrayList<PVector>();

	public void initSpawnPoints() {
		final int bs = BLOCK_SIZE;

		for (Cell c : cells()) {
			
			if (isSpawn(c.x, c.y)) 
				playerSpawn = new PVector(c.x * bs + bs/2, c.y * bs + bs/2);
			
			if (isFinish(c.x, c.y)) 
				bossSpawn = new PVector(c.x * bs + bs/2, c.y * bs + bs/2);
			
			if (walkable(c.x, c.y)) {
				float ux = (float) (c.x * bs + Math.random() * bs);
				float uy = (float) (c.y * bs + Math.random() * bs);
				PVector p = new PVector(ux, uy);
				spawnPoints.add(p);
			}
		}
		
		if (spawnPoints.size() == 0)
			spawnPoints.add(new PVector());
	}

	public PVector getPlayerSpawn() {
		return playerSpawn;
	}
	
	public PVector getBossSpawn() {
		return bossSpawn;
	}
	
	public List<PVector> getSpawnPoints() {
		return spawnPoints;
	}

	public PVector randomSpawnPoint() {
		int r = (int) (Math.random() * spawnPoints.size());
		return spawnPoints.get(r);
	}

	private final int Y_BOUND = Main.HEIGHT / BLOCK_SIZE / 2 + 1;
	private final int X_BOUND = Main.WIDTH / BLOCK_SIZE / 2 + 1;

	public void render(PGraphics buffer, float cam_x, float cam_y) {

		// use bit shift in performance critical parts
		int ux = (int) cam_x >> LOG;
		int uy = (int) cam_y >> LOG;
		
		int off_scrnY = uy + Y_BOUND;
		int off_scrnX = ux + X_BOUND;

		for (int y = uy - Y_BOUND; y <= off_scrnY; ++y) 
			for (int x = ux - X_BOUND; x <= off_scrnX; ++x) 
				if (inBounds(x, y)) 
					if (grid[y][x] != null) 
						grid[y][x].render(buffer, x, y);
	}

	public boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < GRID_SIZE && y < GRID_SIZE;
	}

	public boolean inBounds(PVector loc) {
		return inBounds((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public boolean containsTile(int x, int y) {
		return inBounds(x, y) && grid[y][x] != null;
	}

	public boolean containsTile(PVector loc) {
		return containsTile((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public boolean solid(int x, int y) {
		return containsTile(x, y) && grid[y][x].solid;
	}

	public boolean solid(PVector loc) {
		return solid((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public boolean walkable(int x, int y) {
		return inBounds(x, y) && containsTile(x, y) && grid[y][x].walkable;
	}

	public boolean walkable(PVector loc) {
		return walkable((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public boolean walkable(Node n) {
		return walkable((int) n.loc().x, (int) n.loc().y);
	}

	public boolean isSpawn(int x, int y) {
		return inBounds(x, y) && TileFactory.isSpawn(grid[y][x]);
	}

	public boolean isFinish(int x, int y) {
		return inBounds(x, y) && TileFactory.isFinish(grid[y][x]);
	}

	public boolean isFinish(PVector loc) {
		return isFinish((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public void setTile(int x, int y, Tile t) {
		if (inBounds(x, y)) 
			grid[y][x] = t;
	}

	public void setTile(PVector loc, Tile t) {
		setTile((int) loc.x >> LOG, (int) loc.y >> LOG, t);
	}

	public Tile getTile(int x, int y) {
		if (inBounds(x, y))
			return grid[y][x];
		return null;
	}

	public CompositeTile getItem(int x, int y) {
		if (getTile(x, y) instanceof CompositeTile)
			return (CompositeTile) grid[y][x];
		return null;
	}

	public CompositeTile getItem(PVector loc) {
		return getItem((int) loc.x >> LOG, (int) loc.y >> LOG);
	}

	public Tile[][] grid() {
		return grid;
	}
}
