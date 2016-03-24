package tiles;

import general.Main;
import map.Map;
import processing.core.PGraphics;
import processing.core.PImage;

public class AnimatedTile extends Tile {

	private PImage[] frames;

	public AnimatedTile(String name, String dir, String format, int length, boolean solid, boolean walkable) {
		super(name, solid, walkable);
		frames = new PImage[length];

		for (int i = 0; i < length; i++) {
			String path = "./res/images/tileset/" + dir + i + format;
			PImage p = Main.getInstance().loadImage(path);
			p.resize(Map.BLOCK_SIZE, Map.BLOCK_SIZE);
			frames[i] = p;
		}
	}

	@Override
	public void render(PGraphics buffer, int x, int y) {
		int i = (int) ((Main.frameCount / 3) % frames.length);
		buffer.image(frames[i], x << Map.LOG, y << Map.LOG);
	}
}
