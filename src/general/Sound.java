package general;


import ddf.minim.AudioSnippet;
import ddf.minim.Minim;
import entities.creatures.players.Player;
import processing.core.PApplet;

public class Sound {
	private static Minim minim;
	private static AudioSnippet 
	arrow, 
	shockwave, 
	rocketFired, 
	smallExplosion, 
	bigExplosion, 
	enemyDeath1, 
	enemyDeath2, 
	enemyDeath3, 
	playerDeath, 
	pickup, 
	laser,
	click, 
	enemyBullet, 
	flamethrower, 
	levelFinished,
	bossLaugh;
	
	public static void loadSounds(PApplet p) {
		minim = new Minim(p);
		arrow = minim.loadSnippet("./res/sounds/arrowfired.wav"); 
		shockwave = minim.loadSnippet("./res/sounds/shockwave.wav"); 
		rocketFired = minim.loadSnippet("./res/sounds/rocketfired.wav"); 
		smallExplosion = minim.loadSnippet("./res/sounds/smallexplosion.wav"); 
		bigExplosion = minim.loadSnippet("./res/sounds/bigexplosion.wav"); 
		enemyDeath1 = minim.loadSnippet("./res/sounds/enemydeath1.wav"); 
		enemyDeath2 = minim.loadSnippet("./res/sounds/enemydeath2.wav"); 
		enemyDeath3 = minim.loadSnippet("./res/sounds/enemydeath3.wav"); 
		playerDeath = minim.loadSnippet("./res/sounds/playerdeath.wav"); 
		pickup = minim.loadSnippet("./res/sounds/pickup.wav"); 
		laser = minim.loadSnippet("./res/sounds/laser.wav"); 
		click = minim.loadSnippet("./res/sounds/buttonclick.wav");
		enemyBullet = minim.loadSnippet("./res/sounds/enemybullet.wav");
		flamethrower = minim.loadSnippet("./res/sounds/flamethrower.wav");
		levelFinished = minim.loadSnippet("./res/sounds/levelfinished.wav");
		bossLaugh = minim.loadSnippet("./res/sounds/bosslaugh.wav");
	}
	
	public static void playEnemyDeath() {
		int r = (int) (Math.random() * 3);
		if (r == 0) {
			enemyDeath1.play(0);
		} else if (r == 1) {
			enemyDeath2.play(0);
		} else {
			enemyDeath3.play(0);
		}
	}
	
	public static void playBossLaugh() {
		bossLaugh.play(0);
	}
	
	public static void playLevelFinished() {
		levelFinished.play(0);
	}
	
	public static void playFlamethrower() {
		if (!flamethrower.isPlaying())
			flamethrower.play(0);
	}
	
	public static void playEnemyBullet() {
		enemyBullet.play(0);
	}
	
	public static void playClick() {
		click.play(0);
	}
	
	public static void playLaser() {
		if (!laser.isPlaying())
			laser.play(0);
	}
	
	public static void playPickupSound() {
		pickup.play(0);
	}
	
	public static boolean playerDead = false;
	public static void playPlayerDeath() {
		if (!playerDead) {
			playerDeath.play(0);
			playerDead = true;
		}
	}
	
	public static void playArrowFired() {
		arrow.play(0);
	}
	
	public static void playRocketFired() {
		rocketFired.play(0);
	}

	public static void playShockwave() {
		shockwave.play(0);
	}

	public static void playSmallExplosion() {
		smallExplosion.play(0);	
	}
	
	public static void playBigExplosion() {
		bigExplosion.play(0);	
	}
	
	public static void stop() {
		arrow.close();  //stops the audio files from playing
		minim.stop(); //stops the minim object from running
	}

}
