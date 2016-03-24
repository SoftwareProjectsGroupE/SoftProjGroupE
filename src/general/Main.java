package general;

import gui.Inventory;
import map.MapFactory;
import processing.core.PApplet;
import processing.core.PGraphics;
import states.MainMenu;
import states.StateStack;
import tiles.TileFactory;

public class Main extends PApplet {

	// size must be multiple of 32
	public static final int WIDTH = 32 * 25;
	public static final int HEIGHT = 32 * 19;

	public static int mouseX;
	public static int mouseY;
	public static boolean mousePressed;
	public static int frameCount;
	public static double frameRate;
	public static final double MIN_FPS = 20.0;

	private static Main main;

	private PGraphics buffer;
	// private static PGraphics staticLayer;

	public static Main getInstance() {
		return main;
	}
	
	// public static PGraphics getStaticLayer() {
	// return staticLayer;
	// }

	public void setup() {
		buffer = createGraphics(WIDTH, HEIGHT, P3D);
		// staticLayer = createGraphics(WIDTH, HEIGHT, P3D);
		main = this;
		MapFactory.loadMaps(this);
		TileFactory.loadTiles();
		Inventory.load_images();
		Sprites.load_sprites(this);
		buffer.beginDraw();
		buffer.textAlign(CENTER, CENTER);
		buffer.smooth();
		buffer.endDraw();
		// frameRate((float) MIN_FPS);
		StateStack.push(new MainMenu(this));
	}

	public void draw() {
		mouseX = super.mouseX;
		mouseY = super.mouseY;
		mousePressed = super.mousePressed;
		frameCount = super.frameCount;
		frameRate = super.frameRate;

		buffer.beginDraw();
		// staticLayer.beginDraw();
		StateStack.head().update(this);
		StateStack.head().render(buffer);
		buffer.fill(255, 0, 0);
		buffer.endDraw();
		// staticLayer.endDraw();
		image(buffer, 0, 0);
		// image(staticLayer, 0 ,0);
	}

	public void mousePressed() {
		StateStack.head().mousePressed(mouseX, mouseY);
	}

	public void mouseDragged() {
		StateStack.head().mouseDragged(mouseX, mouseY);
	}

	public void keyPressed() {
		StateStack.head().keyPressed(key);
	}

	public void keyReleased() {
		StateStack.head().keyReleased(key);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "general.Main" });
	}

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}
}
