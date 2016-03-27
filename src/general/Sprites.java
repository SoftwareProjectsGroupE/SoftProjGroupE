package general;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Sprites {
	
	public static PImage DINO_STD;
	public static PImage DINO_RSE;
	public static PImage DINO_SUIC;
	public static PImage DINO_CRWL;
	public static PImage DINO_BOSS;
	
	public static PImage MEDIEV_STD;
	public static PImage MEDIEV_RSE;
	public static PImage MEDIEV_SUIC;
	public static PImage MEDIEV_CRWL;
	public static PImage MEDIEV_BOSS;
	
	public static PImage UNDR_STD;
	public static PImage UNDR_RSE;
	public static PImage UNDR_SUIC;
	public static PImage UNDR_CRWL;
	public static PImage UNDR_BOSS;
	
	public static PImage FINL_BOSS;
	public static PImage FINL_CRWL;
	
	public static PImage MP_ENEMY;
	
	public static PImage ARROW;

	static PImage[][] up = new PImage[8][9];
	static PImage[][] down = new PImage[8][9];
	
	/*static List<List<PImage>> enemies = new ArrayList<>();
	static List<PImage> bosses = new ArrayList<>();
	static List<PImage> crawlers = new ArrayList<>();*/

	public static void load_sprites(PApplet p) {
		load_player(p);
		DINO_STD = p.loadImage("./res/images/sprites/enemies/dino/std.bmp");
		DINO_RSE = p.loadImage("./res/images/sprites/enemies/dino/rse.bmp");
		DINO_SUIC = p.loadImage("./res/images/sprites/enemies/dino/suic.bmp");
		DINO_CRWL = p.loadImage("./res/images/sprites/enemies/dino/crawl.bmp");
		DINO_BOSS = p.loadImage("./res/images/sprites/enemies/dino/boss.bmp");
		
		MEDIEV_STD = p.loadImage("./res/images/sprites/enemies/mediev/std.bmp");
		MEDIEV_RSE = p.loadImage("./res/images/sprites/enemies/mediev/rse.bmp");
		MEDIEV_SUIC = p.loadImage("./res/images/sprites/enemies/mediev/suic.bmp");
		MEDIEV_CRWL = p.loadImage("./res/images/sprites/enemies/mediev/crawl.bmp");
		MEDIEV_BOSS = p.loadImage("./res/images/sprites/enemies/mediev/boss.bmp");
		
		UNDR_STD = p.loadImage("./res/images/sprites/enemies/under/std.bmp");
		UNDR_RSE = p.loadImage("./res/images/sprites/enemies/under/rse.bmp");
		UNDR_SUIC = p.loadImage("./res/images/sprites/enemies/under/suic.bmp");
		UNDR_CRWL = p.loadImage("./res/images/sprites/enemies/under/crawl.bmp");
		UNDR_BOSS = p.loadImage("./res/images/sprites/enemies/under/boss.bmp");
		
		FINL_BOSS = p.loadImage("./res/images/sprites/enemies/finalboss/boss.bmp");
		FINL_CRWL = p.loadImage("./res/images/sprites/enemies/finalboss/crawl.bmp");
		
		MP_ENEMY = p.loadImage("./res/images/sprites/enemies/multiplayer/enemy.bmp");
		
		ARROW = p.loadImage("./res/images/sprites/arrow.bmp");
		ARROW.resize(40, 40);
		//load_enemies(p);
	}
	
	private static void load_player(PApplet p) {
		File dir = new File("./res/images/sprites/dragon/");
		File[] files = dir.listFiles();
		Arrays.sort(files);
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
	
	/*private static void load_enemies(PApplet p) {
		File dir = new File("./res/images/sprites/enemies/");
		File[] folders = dir.listFiles();
		Arrays.sort(folders);
		for (File folder : folders) {
			File[] files = folder.listFiles();
			Arrays.sort(files);
			List<PImage> list = new ArrayList<>();
			for (File file : files) {
				PImage img = p.loadImage(file.getPath());
				list.add(remove_backgroud(p, img));
			}
			enemies.add(list);
		}
	}
	
	public static PImage get_rand_enemy(int catagory) {
		List<PImage> list = enemies.get(catagory);
		return list.get((int)(Math.random() * list.size()));
	}*/
	
	
	
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
