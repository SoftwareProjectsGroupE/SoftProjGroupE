package general;

public class GameConstants {
	
	public static final int PLAYER_SPEED_SP = 20; // 3;

	public static final double MP_BULLET_DAMAGE = 0.1;
	public static final double MP_FIRE_DAMAGE = 0.01;
	
	// set lower to recalculate astar more frequently
	public static final int A_STAR_INTERVAL = 15;
	
	public static final int LASER_AMMO_DCRMNT_SP = 1;
	public static final double LASER_DAMAGE_SP = 0.06;
	public static final int DEFAULT_AMMO = 1000;
	public static final int FLAMETHROWER_AMMO_DCRMNT_MP = 1;
	public static final float CAMERA_EASING = 10;
	
	public static final double BOSS_AUTOMATIC_DAMAGE = 0.05;
	public static final double BOSS_ROCKET_DAMAGE = 0.1;
	public static final double BOSS_FLAMETHROWER_DAMAGE = 0.001;
	public static final double BOSS_SHOCKWAVER_DAMAGE = 0.08;
	public static final double BOSS_LASER_DAMAGE = 0.005;
	public static final double BOSS_COLLISION_DAMAGE = -0.03;
	public static final int BOSS_HEALTH = 50;
	public static final int BOSS_SPEED = 7;
	
	public static final double CRAWLER_COLLISION_DAMAMGE = -0.005;
	public static final double STANDARD_ENEMY_COLLISION_DAMAGE = -0.005;
	public static final double RANDOM_STATE_ENEMY_COLLISION_DAMAGE = -0.005;
	public static final double SUICIDE_BOMBER_COLLISION_DAMAGE = -0.1;
	public static final double SUICIDE_BOMBER_SPEED = 2;
	public static final double RANDOM_STATE_ENEMY_SPEED = 2.5;
	
}
