package general;

import gui.Inventory;
import map.MapFactory;
import processing.core.PApplet;
import processing.core.PGraphics;
import states.MainMenu;
import states.StateStack;
import tiles.TileFactory;

public class Main extends PApplet {

	// size must be multiple of 32 (just dont change this)
	public static final int WIDTH = 32 * 25;
	public static final int HEIGHT = 32 * 19;

	public static int mouseX;
	public static int mouseY;
	public static boolean mousePressed;
	public static int frameCount;
	public static double frameRate;
	public static double spriteFrameCounter = 0;
	public static final double MIN_FPS = 20.0;

	private static Main main;

	private PGraphics buffer;

	public static Main getInstance() {
		return main;
	}

	public void setup() {
		surface.setTitle("World Warriors");
		buffer = createGraphics(WIDTH, HEIGHT, P3D);
		main = this;
		MapFactory.loadMaps(this);
		TileFactory.loadTiles();
		Inventory.load_images();
		Sprites.load_sprites(this);
		Sound.loadSounds(this);
		buffer.beginDraw();
		buffer.textAlign(CENTER, CENTER);
		buffer.smooth();
		buffer.endDraw();
		//frameRate((float) 30);
		StateStack.push(new MainMenu(this));
	}

	public void draw() {
		mouseX = super.mouseX;
		mouseY = super.mouseY;
		mousePressed = super.mousePressed;
		frameCount = super.frameCount;
		frameRate = super.frameRate;
		
        double f = frameRate;	
		if(f < MIN_FPS)
			f = MIN_FPS;
		spriteFrameCounter += 60.0/f;

		buffer.beginDraw();
		StateStack.head().update(this);
		StateStack.head().render(buffer);
		buffer.fill(255, 0, 0);
		buffer.endDraw();
		image(buffer, 0, 0);
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
	
	public void stop() { // when the program is closed, the following code is run
	    Sound.stop();
		super.stop();// runs the superclass' stop method
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "general.Main" });
	}

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}
}
