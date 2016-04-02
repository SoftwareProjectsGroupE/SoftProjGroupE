package multiplayer;

public class Protocol {
	public static final byte PLAYER_CONNECTED = 0;
	public static final byte PLAYER_MOVED = 1;
	public static final byte BULLET_FIRED = 2;
	public static final byte PLAYER_DISCONNECTED = 3;
	public static final byte CHAT_MESSAGE = 4;
	public static final byte ADD_POINT = 5;
	public static final byte IN_INTERMISSION = 6;
	public static final byte FLAME_FIRED = 7;
	public static final byte ITEM_PICKED_UP = 8;
	public static final byte HEALTH_UPDATED = 9;
	public static final byte PLAYER_RESPAWNED = 10;

	public static final long INTERMISSION_TIME = 60000; // 1 minute

	public static final int MAX_PLAYERS = 20;
	public static final int PORT = 6789;
	public static final int MAP_COUNT = 4;

	public static final String SCRIPT_URL = "http://doc.gold.ac.uk/~ma301ab/PHPTest/game/index.php?";
	public static final String IP_LOOKUP_URL = "http://checkip.amazonaws.com";
}
