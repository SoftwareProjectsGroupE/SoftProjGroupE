package general;

import java.io.File;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Sprites {
	
	
	public static final PImage STD_ENEMY = Main.getInstance().loadImage("./res/images/sprites/enemies/test.bmp");

	static PImage[][] up = new PImage[8][9];
	static PImage[][] down = new PImage[8][9];

	public static void load_sprites(PApplet p) {
		File dir = new File("./res/images/sprites/dragon/");
		File[] files = dir.listFiles();
		for (int k = 0; k < 2; k++) {
			PImage[][] list = k == 0 ? up : down;
			for (int i = 0; i < 8; i++) {
				list[i] = new PImage[9];
				for (int j = 0; j < 9; j++) {
					PImage img = p.loadImage(files[k * 72 + i * 9 + j].getPath());
					list[i][j] = remove_backgroud(p, img);
				}
			}
		}
	}
	
	
	
	public static PImage remove_backgroud(PApplet p, PImage img) {
		PImage new_img = p.createImage(img.width, img.height, PConstants.ARGB);
		new_img.loadPixels();
		img.loadPixels();
		for (int i = 0; i < img.pixels.length; i++) {
			// extract the rgb components of this pixel
			int px = img.pixels[i];
			float r = p.red(px); 
			float g = p.green(px);
			float b = p.blue(px);

			if (!(r == 106 && g == 76 && b == 48))
				new_img.pixels[i] = px;
		}
		new_img.updatePixels();
		return new_img;
	}

	static int last_dir = 0;

	public static PImage get(int dir) {
		if (dir == -1)
			dir = last_dir;
		
		last_dir = dir;
		
        double f = Main.frameRate;
		
		if(f < Main.MIN_FPS)
			f = Main.MIN_FPS;
		
		int indx = ((int)Main.spriteFrameCounter/2) % 18;

		return indx < 9 ? up[dir][indx] : down[dir][indx - 9];

		/*
		 * dir: 0 - 8 = N - W
		 * 
		 * 
		 * 
		 */
	}

}
