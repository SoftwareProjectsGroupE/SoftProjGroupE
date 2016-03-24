package map;

import general.Main;
import processing.core.PGraphics;
import states.State;
import states.StateStack;
import tiles.Tile;
import tiles.TileFactory;

public class TilePicker implements State {
	
	private Tile[][] pallet = new Tile[Main.HEIGHT/Map.BLOCK_SIZE][Main.WIDTH/Map.BLOCK_SIZE];
	private MapEditor mapEditor;
	
	
	public TilePicker(MapEditor me) {
		mapEditor = me;
		for (int i = 0; i < TileFactory.tileCount(); i++) {
			int y = i / pallet[0].length;
			int x = i % pallet[0].length;
			pallet[y][x] = TileFactory.getTile(String.valueOf(i));
		}
	}
	
	

	@Override
	public void render(PGraphics p) {
		p.background(50);
		
		for (int y = 0; y < pallet.length; y++) 
			for (int x = 0; x < pallet[0].length; x++) 
				if (pallet[y][x] != null) 
					pallet[y][x].render(p, x, y);

		int mx = Main.mouseX/Map.BLOCK_SIZE;
		int my = Main.mouseY/Map.BLOCK_SIZE;
		
		if (in_bounds(mx, my)) {
			Tile tile = pallet[my][mx];
			if (tile != null) {
				String s = "";
				if (!tile.walkable)
					s += " (UNWALKABLE)";
				p.text(tile.get_name() + s, Main.mouseX + 30, Main.mouseY + 30);
			}
		}
	}
	
	

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		int mx = mouseX/Map.BLOCK_SIZE;
		int my = mouseY/Map.BLOCK_SIZE;
		if (in_bounds(mx, my)) {
			if (pallet[my][mx] != null) {
				mapEditor.set_editor_tile(pallet[my][mx]);
				StateStack.pop();
			}
		}
	}
	
	
	
	private boolean in_bounds(int mx, int my) {
		return mx >= 0 && my >= 0 && mx < pallet[0].length && my < pallet.length;
	}
	
	

	public void onExit() {}
	public void keyPressed(int key) {}
	public void keyReleased(int key) {}
	public void onStart() {}
	public void update(Main m) {}
	public void mouseDragged(int mouseX, int mouseY) {}
}
