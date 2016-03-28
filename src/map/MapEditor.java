package map;

import java.util.List;

import general.Controller;
import general.Main;
import general.PathFinder;
import general.Sound;
import gui.Button;
import gui.Function;
import map.Map.Cell;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import states.MessageScreen;
import states.State;
import states.StateStack;
import tiles.CompositeTile;
import tiles.Tile;
import tiles.TileFactory;
import tiles.items.ItemTile;



public class MapEditor implements State {
	
	private final boolean DEV_MODE = false;
	
	private Controller controller = new Controller();
	
	private Button save_btn = new Button("Save Map", 0, 0, 100, 25);
	private Button change_tile_btn = new Button("Change Tile", 0, 25, 100, 25);
	private Button back_btn = new Button("Back", Main.WIDTH - 100, 0, 100, 25);
	private Button inc_size_btn = new Button("+size", 50, 50, 50, 25);
	private Button dec_size_btn = new Button("-size", 0, 50, 50, 25);
	
	private Tile editor_tile = TileFactory.getTile("0");

	private final Main m;
	private final Map map;
	private final Map itemLayer;
	
	private int unitX, unitY, selection_size = 1;


	
	
	public MapEditor(Main m, Map map) {
		this.m = m;
		this.map = map;
		itemLayer = new Map();
		
		for (Cell cell : map.cells()) {
			CompositeTile t = map.getItem(cell.x, cell.y);
			if (t != null) {
				this.map.setTile(cell.x, cell.y, t.base);
				itemLayer.setTile(cell.x, cell.y, t.item);
			}
		}
		Sound.playTheme(-1);
	}
	
	public MapEditor(Main m) {
		this(m, new Map());
	}
	
	
	
	
	@Override
	public void update(Main m) {
		if (controller.up) 
			unitY--;
		if (controller.down)
			unitY++;
		if (controller.left) 
			unitX--;
		if (controller.right)
			unitX++;
	}
	
	
		
	
	@Override
	public void render(PGraphics p) {
		final int bs = Map.BLOCK_SIZE;
		
		// Scale up to pixel precision.
		int x = unitX * bs;
		int y = unitY * bs;
		
		p.pushMatrix();
		
		p.translate(Main.WIDTH/2 + bs/2, Main.HEIGHT/2 + bs/2);
		p.translate(-x, -y);
		
		p.background(0);
		
		map.render(p, x, y);
		itemLayer.render(p, x, y);
		
		// draw grid
		p.stroke(255);
		p.fill(255, 0);
		for (Cell c : map.cells()) 
			p.rect(c.x * bs, c.y * bs, bs, bs);
		
		p.popMatrix();
		
		// Display buttons.
		back_btn.render(p, p.color(255, 0, 0));
		save_btn.render(p, p.color(255, 0, 0));
		change_tile_btn.render(p, p.color(255, 0, 0));
		inc_size_btn.render(p, p.color(255, 0, 0));
		dec_size_btn.render(p, p.color(255, 0, 0));
		
		// Render the current object being used for editing.
		renderEditorObject(p, Main.mouseX, Main.mouseY);
		p.fill(0, 255, 0);
		p.text("Right click/drag to delete objects.", 200, 20);
	}
	
	

	
	private int[][] get_spawn_and_finish() {
		int[][] points = new int[2][]; 
		
		for (Cell c : map.cells())
				
			if (map.isSpawn(c.x, c.y)) 
				points[0] = new int[] {c.x, c.y};
		
			else if (map.isFinish(c.x, c.y)) 
				points[1] = new int[] {c.x, c.y};

		return points[0] == null || points[1] == null ? null : points;
	}
	
	
	
	
	private void renderEditorObject(PGraphics p, int mouseX, int mouseY) {
		int mx = mouseX/Map.BLOCK_SIZE;
		int my = mouseY/Map.BLOCK_SIZE;
			
		p.fill(0, 255, 0);
		String s = "";
		if (!editor_tile.walkable)
			s = " (UNWALKABLE)";
		p.text(editor_tile.get_name() + s, mouseX, mouseY - 50);
		
		if (editor_tile instanceof ItemTile) {
			editor_tile.render(p, mx, my);
			selection_size = 1;
			return;
		}
		
		for (int y = my; y < my + selection_size; y++) 
			for (int x = mx; x < mx + selection_size; x++) 
				editor_tile.render(p, x, y);
	}
	
	

	
	private void edit(int mouseX, int mouseY) {
		
		// Don't edit the map if the mouse is over any of the buttons.
		for (Button b : new Button[] {change_tile_btn, save_btn, back_btn, inc_size_btn, dec_size_btn})
			if (b.mouseOver(mouseX, mouseY)) 
				return;
		
		// Calc the position of the mouse in terms of grid units.
		int mx = (mouseX >> Map.LOG) + unitX - Main.WIDTH/2/Map.BLOCK_SIZE - 1;
		int my = (mouseY >> Map.LOG) + unitY - Main.HEIGHT/2/Map.BLOCK_SIZE - 1;
		
		boolean editingSpawn = TileFactory.isSpawn(editor_tile);
		boolean editingFinish = TileFactory.isFinish(editor_tile);
		
		if (m.mouseButton == PApplet.RIGHT) {
			setAll(mx, my, selection_size, null);
			return;
		} 
		
		if (editor_tile instanceof ItemTile) {
			
			CompositeTile c = map.getItem(mx, my);
			
			if (c != null)
				map.setTile(mx, my, c.base);
			
			if (map.walkable(mx, my)) 			
				itemLayer.setTile(mx, my, editor_tile);

			return;
		} 
		
		if (editingSpawn) {
			remove_tile(new RemovalTile() {
				public boolean matches(int x, int y) {
					return map.isSpawn(x, y);
				}
			});
		}
		
		if (editingFinish) {
			remove_tile(new RemovalTile() {
				public boolean matches(int x, int y) {
					return map.isFinish(x, y);
				}
			});
		}
		
		if (editingSpawn || editingFinish) {
			selection_size = 1;
			map.setTile(mx, my, editor_tile);
			return;
		}
		
		setAll(mx, my, selection_size, editor_tile);
	}
	
	
	
	
	private void setAll(int mx, int my, int size, Tile editorTile) {
		
		for (int y = my; y < my + size; y++) {
			for (int x = mx; x < mx + size; x++) {
				
				map.setTile(x, y, editorTile);
	
				if (editorTile == null) {
					itemLayer.setTile(x, y, null);
					continue;
				} 
				
				if (!editorTile.walkable) 
					itemLayer.setTile(x, y, null);
			}
		}
	}
	
	
	
	
	private interface RemovalTile {
		boolean matches(int x, int y);
	}
	
	private void remove_tile(RemovalTile rt) {
		for (Cell c : map.cells())
			if (rt.matches(c.x, c.y))
				map.setTile(c.x, c.y, null);
	}
	
	


	public void saveAsFile(String name) {
		final String[] data = copyGridData(map.grid(), itemLayer.grid());
		
		List<String> map_files;
		final String dir;
		
		if (DEV_MODE) {
			map_files = MapFactory.getDevMapFiles();
			dir = "devmaps";
		} else {
			map_files = MapFactory.getCustomMapFiles();
			dir = "custommaps";
		}
		
		final String new_file_name = "/res/maps/" + dir + "/" + name.toLowerCase() + ".txt";
		
		m.saveStrings(new_file_name, data);
		
		// only run this if the new file name isn't already in mapFiles
		if (!map_files.contains(new_file_name)) 
			map_files.add(new_file_name);
		
		StateStack.setCurrentState(new MessageScreen(new_file_name + " SAVED."));
	}
	

	
	
	

	private String[] copyGridData(Tile[][] grid, Tile[][] itemGrid) {
		int gs = Map.GRID_SIZE;
		String[] copy = new String[gs * gs];
		
		for (Cell c : map.cells()) {

			if (grid[c.x][c.y] == null) {
				copy[c.y + c.x * gs] = null;
				continue;
			}
			
			String data = grid[c.x][c.y].get_id();
			
			if (itemGrid[c.x][c.y] != null) 
				data += itemGrid[c.x][c.y].get_id();
			
			copy[c.y + c.x * gs] = data;
		}

		return copy;
	}
	
	
	
	
	public void set_editor_tile(Tile tile) {
		editor_tile = tile;
	}
	
	
	

	public void keyPressed(int key) {
		controller.keyPressed(key);
	}

	public void keyReleased(int key) {
		controller.keyReleased(key);
	}
	
	
	
	
	private boolean path_to_finish_exists() {
		PathFinder path_finder = new PathFinder(false);
		
		final int[][] points = get_spawn_and_finish();
		
		int[] spawn = points[0];
		int[] finish = points[1];
		
		PVector spawn_vec = new PVector(spawn[0], spawn[1]);
		PVector finish_vec = new PVector(finish[0], finish[1]);
		
		return path_finder.a_star_search(map, spawn_vec, finish_vec) != null;
	}
	
	
	

	public void mousePressed(int mouseX, int mouseY) {
		edit(mouseX, mouseY);
		
		MapEditor me = this;
		
		if (save_btn.mouseOver(mouseX, mouseY)) {
			
			save_btn.press(new Function() {
				public void invoke() {
					
					Function back = new Function() {
						public void invoke() {
							StateStack.pop();
						}
					};
					
					if(get_spawn_and_finish() == null) {
						StateStack.push(new MessageScreen("Please add Spawn Area and Finish Area Tiles!", back));
						editor_tile = TileFactory.getTile("0");
						return;
					}
					
					if(!path_to_finish_exists()) {
						StateStack.push(new MessageScreen("No path to the finish point exists!\n"
								                        + "Make sure there are no walls closing off "
								                        + "the finish or spawn point!", back));
						return;
					}
					
					StateStack.push(new MapNamingScreen(me));
				}
			});
		}
		
		if (change_tile_btn.mouseOver(mouseX, mouseY)) {
			change_tile_btn.press(new Function() {
				public void invoke() {
					StateStack.push(new TilePicker(me));
				}
			});
		}
		
		if (back_btn.mouseOver(mouseX, mouseY)) 
			back_btn.press(new Function(){});
		
		if (inc_size_btn.mouseOver(mouseX, mouseY)) {
			inc_size_btn.press(new Function() {
				public void invoke() {
					selection_size++;
				}
			});
		}
		
		if (dec_size_btn.mouseOver(mouseX, mouseY)) {
			dec_size_btn.press(new Function() {
				public void invoke() {
					if(--selection_size < 1)
						selection_size = 1;
				}
			});
		}
	}
	
	
	

	public void mouseDragged(int mouseX, int mouseY) {
		edit(mouseX, mouseY);
	}
	
	
	

	public void onStart() {}
	public void onExit() {}
}