package multiplayer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import entities.bullets.Bullet;
import entities.bullets.BulletMP;
import entities.bullets.FireMP;
import entities.creatures.players.PlayerMP;
import general.Game;
import general.GameConstants;
import general.Main;
import general.Sound;
import general.Sprites;
import gui.Button;
import gui.Function;
import map.Map;
import map.MapFactory;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import states.MainMenu;
import states.MessageScreen;
import states.StateStack;
import tiles.CompositeTile;
import tiles.items.HealthPack;

public class GameMP extends Game {
	
	private final Button scoreboard_btn = new Button("Scoreboard", 0, 0, 75, 25);

	private Socket socket;
	private DataOutputStream out;
	private final Object OUT_LOCK = new Object();

	private String error = "";

	private String hostIP;

	private int id;

	// bullets needs synchronization as inputReader thread writes (adds bullets)
	// while main thread is reading/removing
	private final Object BULLETS_LOCK = new Object();
	private final List<Bullet> bullets = new ArrayList<Bullet>();

	private final int[] playersX = new int[Protocol.MAX_PLAYERS];
	private final int[] playersY = new int[Protocol.MAX_PLAYERS];
	private final double[] players_health = new double[Protocol.MAX_PLAYERS];
	private final int[] players_scores = new int[Protocol.MAX_PLAYERS];
	
	private List<Object[]> sorted_scoreboard = new ArrayList<Object[]>();
	private final Comparator<Object[]> score_sorter = new Comparator<Object[]>() {
		public int compare(Object[] o1, Object[] o2) {
			return (int) o1[1] - (int) o2[1];		
		}
	};
	

	private boolean showing_scoreboard = false;
	
	private ChatBox chat;

	private Map map;

	private long elapsed_intermission_time;
	private boolean in_intermission = false;

	private int current_map_id;

	private List<int[]> removed_items = new ArrayList<int[]>();
	private final Object REMOVED_ITEMS_LOCK = new Object();
	
	private List<String> intermission_scoreboard = new ArrayList<String>();

	
	
	
	public GameMP() {

		ArrayList<String> potential_hosts = get_potential_hosts();
		if (potential_hosts == null)
			return;

		if (!connect_to_host(potential_hosts))
			return;

		player = new PlayerMP(1, 16, 4);

		DataInputStream in = setup_streams();
		if (in == null)
			return;

		chat = new ChatBox(this);
		chat.append_feed("Successfully connected to host: " + hostIP.substring(0, 5) + "...");
		chat.enable();

		InputHandler ih = new InputHandler(in);
		new Thread(ih, "Client input handler thread").start();
		
		// stop menu theme
		Sound.playTheme(-1);
		Sound.playThemeMP(current_map_id);
	}
	
	
	
	
	private DataInputStream setup_streams() { 
		DataInputStream in = null;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			in = new DataInputStream(socket.getInputStream());
			
			read_current_game_state(in);
			
		} catch (IOException e) {
			// if this exception is caught it may be because the server closed the
			// socket as soon as it accepted it so signify that max_players is reached
			e.printStackTrace();
			appendError("Failed to setup game or server has reached MAX_PLAYERS.\nPlayer rejected.", e);
		}
		return in;
	}
	
	
	
	
	
	private void read_current_game_state(DataInputStream in) throws IOException {
		id = in.readInt();
		current_map_id = in.readInt();
		elapsed_intermission_time = in.readLong();
		
		for (int i = 0; i < Protocol.MAX_PLAYERS; i++) {
			players_scores[i] = in.readInt();
			players_health[i] = in.readDouble();
		}
		
		resort_sorted_scoreboard();
		
		int len = in.readInt();
		for (int i = 0; i < len; i++)
			intermission_scoreboard.add(in.readUTF());
		
		int length = in.readInt();
		for (int i = 0; i < length; i++)
			removed_items.add(new int[] { in.readInt(), in.readInt() });
	}
	
	
	

	private boolean connect_to_host(ArrayList<String> potentialHosts) {
		for (String ip : potentialHosts) {
			String hiddenIP = ip.substring(0, 5);
			socket = new Socket();
			try {
				System.out.println("Checking if " + hiddenIP + "... is an active host...");
				
				// connect to the ServerSocket that is listening on specified
				// port. timeout after 2 seconds to prevent long freeze
				socket.connect(new InetSocketAddress(ip, Protocol.PORT), 2000);
				// s = new Socket(hostIP, PORT);
				
				hostIP = ip;
				System.out.println(hiddenIP + "... is an active host.");
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println(hiddenIP + "... is inactive.");
			}
		}
		appendError("Failed to find and connect to a host.\n");
		return false;
	}
	
	
	

	private ArrayList<String> get_potential_hosts() {
		ArrayList<String> potentialHosts = new ArrayList<String>();
		String line = null;

		try {
			URL url = new URL(Protocol.SCRIPT_URL + "get_potential_local_hosts&get_potential_public_host");
			URLConnection con = url.openConnection();
			BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));

			while ((line = response.readLine()) != null)
				if (line.trim().matches("[0-9\\.]+"))
					potentialHosts.add(line.trim());

		} catch (IOException e) {
			e.printStackTrace();
			appendError("Remote script failed.\nPlease ensure you're connected to the internet.", e);
			return null;
		}
		if (potentialHosts.size() == 0) {
			appendError("No server is running.");
			return null;
		}
		return potentialHosts;
	}
	
	
	

	private void appendError(String err, Exception e) {
		appendError(err + "\n" + e.getMessage());
	}

	private void appendError(String err) {
		error += err + "\n";
	}
	
	
	

	@Override
	public void onStart() {
		if (error.equals("")) {
			map = MapFactory.getMultMap(current_map_id);
			bullets.clear();
			in_intermission = false;
			spawn_player();
		}
	}
	
	
	

	@Override
	public void onExit() {
		if (error.equals("")) {
			for (int i = 0; i < players_scores.length; i++)
				if (players_scores[i] != -1)
					players_scores[i] = 0;
			
			resort_sorted_scoreboard();

			intermission_scoreboard = new ArrayList<String>();
			
			Arrays.fill(players_health, 1);
			
			player.asMP().reset();
			
			synchronized (REMOVED_ITEMS_LOCK) {
				removed_items = new ArrayList<int[]>();
			}
		}
	}
	
	
	

	public void update(Main m) {
		if (!error.equals("")) {
			handle_error();
			return;
		}

		if (in_intermission || elapsed_intermission_time != -1) {
			StateStack.push(new IntermissionScreen(this, intermission_scoreboard, elapsed_intermission_time));
			elapsed_intermission_time = -1;
			return;
		}

		player.update(this);
		player.collidedWall(map);

		check_tile_for_item();
		check_removed_items();

		update_bullets();

		check_player_moved();

		synchronized (BULLETS_LOCK) {
			for (int i = bullets.size() - 1; i >= 0; i--) 
				if (bullets.get(i).removed()) 
					bullets.remove(i);
		}
	}
	
	
	
	
	private void handle_error() {
		StateStack.setCurrentState(new MessageScreen(error));
		if (chat != null)
			chat.close();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	private void check_tile_for_item() {
		CompositeTile tile = map.getItem(player.loc());
		if (tile != null) {
			tile.onPickup(player);
			
			if (tile.item instanceof HealthPack) 
				send_health_updated(id, 0.3);
			
			send_item_picked_up(player.loc());
			
			Sound.playPickupSound();
		}
	}
	
	
	
	
	private void check_removed_items() {
		synchronized (REMOVED_ITEMS_LOCK) {
			if (removed_items.size() > 0) {
				for (int[] a : removed_items) {
					CompositeTile item = map.getItem(a[0], a[1]);
					
					if (item != null) 
						map.setTile(a[0], a[1], item.base);
				}
				removed_items = new ArrayList<int[]>();
			}
		}
	}
	
	
	
	
	private void check_player_moved() {
		PlayerMP playerMP = player.asMP();
		if (playerMP.moved()) 
			send_player_moved();

		if (playerMP.respawned()) {
			playerMP.setRespawned(false);
			send_player_respawned();
		}
	}
	
	
	

	private void update_bullets() {
		synchronized (BULLETS_LOCK) {
			for (int i = 0; i < bullets.size(); i++) {
				
				BulletMP b = (BulletMP) bullets.get(i);
				b.update(this);
				
				if (map.solid(b.loc()) || b.offScreen(screen_loc())) {
					b.onRemove(this);
					continue;
				}

				for (int j = 0; j < Protocol.MAX_PLAYERS; j++) {
					if (b.SHOOTER_ID == j)
						continue;

					int x = playersX[j];
					int y = playersY[j];

					if (x == 0 && y == 0)
						continue;

					PVector player_loc = new PVector(x, y);
					if (player_loc.dist(b.loc()) < 16 + b.getRadius()) {

						if (j == id)
							send_health_updated(b.SHOOTER_ID, -b.DAMAGE);

						b.onRemove(this);
						break;
					}
				}
			}
		}
	}
	
	

	
	private void resort_sorted_scoreboard() {
		sorted_scoreboard = new ArrayList<Object[]>();
		
		for (int i = 0; i < players_scores.length; i++)
			if (players_scores[i] != -1)
				sorted_scoreboard.add(new Object[]{"Player " + i + ":", players_scores[i]});
		
		sorted_scoreboard.sort(score_sorter);
	}
	
	
	

	@Override
	public void render(PGraphics p) {

		p.pushMatrix();

		p.translate(Main.WIDTH / 2, Main.HEIGHT / 2);
		p.translate(-player.locX(), -player.locY());

		p.background(0);
		map.render(p, player.locX(), player.locY());

		synchronized (BULLETS_LOCK) {
			for (Bullet b : bullets)
				b.render(p);
		}

		draw_players(p);

		p.popMatrix();

		player.render(p);
		render_health_bar(p, id, Main.WIDTH / 2, Main.HEIGHT / 2);

		p.fill(255, 0, 0);
		p.textSize(15);
		p.text(id, Main.WIDTH / 2, Main.HEIGHT / 2);
		p.textSize(12);	
		//p.text("fps: " + Main.frameRate, 100, 30);
		//p.text("Map: " + get_map().name, 100, 55);
		
		if (showing_scoreboard) 
			show_scoreboard(p);
			
		exitButton.render(p, p.color(255, 0, 0));
		inventoryButton.render(p, p.color(255, 0, 0));
		scoreboard_btn.render(p, p.color(255, 0, 0));

		inventory.render(p, player);

		player.getGun().render(p);
	}
	
	
	

	private void draw_players(PGraphics p) {
		for (int i = 0; i < Protocol.MAX_PLAYERS; i++) {
			
			// i can draw myself, thank you very much
			if (i == id) 
				continue;
			
			int x = playersX[i];
			int y = playersY[i];
			
			if (x == 0 && y == 0) 
				continue;
			
			if (!on_screen(screen_loc(), x, y)) 
				continue;

			render_health_bar(p, i, x, y);

			// player
			p.pushMatrix();
			p.translate(x, y);
			double a = PApplet.atan2(player.locY() - y, player.locX() - x);
			p.rotate((float)a + PApplet.radians(90));
			PImage img = Sprites.MP_ENEMY;
			p.image(img, -img.width/2, -img.height/2);
			p.popMatrix();
			p.fill(0, 255, 0);
			p.textSize(15);
			p.text(i, x, y);
			p.textSize(12);
			p.fill(0);
		}
	}
	
	
	
	
	private void show_scoreboard(PGraphics p) {
		if (Main.frameCount % 60 == 0)
			resort_sorted_scoreboard();
		
		String scoreboard = "";
		
		for (int i = sorted_scoreboard.size() - 1; i >= 0; i--) {
			Object[] pair = sorted_scoreboard.get(i);
			scoreboard += (String) pair[0] + pair[1] + "\n";
		}
		p.fill(0, 150);
		p.rectMode(PApplet.CENTER);
		p.rect(Main.WIDTH/2, Main.HEIGHT/2, 200, sorted_scoreboard.size() * 30);
		p.rectMode(PApplet.CORNER);
		p.fill(255);
		p.textSize(15);
		p.text(scoreboard, Main.WIDTH/2, Main.HEIGHT/2);
		p.textSize(12);
	}
	
	
	

	private void render_health_bar(PGraphics p, int id, int x, int y) {
		p.fill(255, 0, 0);
		int width = 50;
		p.rect(x - width / 2, y - 26, width, 5);
		p.fill(0, 255, 0);
		float w = PApplet.map((float) players_health[id], 0, 1, 0, width);
		if (w < 0)
			w = 0;
		p.rect(x - width / 2, y - 26, w, 5);
	}
	
	
	

	public void set_loc(int id, int x, int y) {
		if (id < 0 || id >= Protocol.MAX_PLAYERS) {
			appendError("Player ID: " + id + " out of bounds.");
		} else {
			playersX[id] = x;
			playersY[id] = y;
		}
	}
	
	
	

	public static boolean on_screen(PVector screen, int x, int y) {
		int offset = 50;
		return x + offset > screen.x && 
			   x - offset < screen.x + Main.WIDTH &&
			   y + offset > screen.y && 
			   y - offset < screen.y + Main.HEIGHT;
	}
	
	
	

	private void spawn_player() {
		PVector spawn = map.randomSpawnPoint();
		player.setLocX(spawn.x);
		player.setLocY(spawn.y);
		player.asMP().setRespawned(true);
	}
	
	
	

	private void send_health_updated(int shooter_id, double health_incrmnt) {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.HEALTH_UPDATED);
				out.writeInt(id);
				out.writeInt(shooter_id);
				out.writeDouble(health_incrmnt);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	private void send_player_respawned() {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.PLAYER_RESPAWNED);
				out.writeInt(id);
				out.writeInt((int) player.locX());
				out.writeInt((int) player.locY());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	private void send_player_moved() {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.PLAYER_MOVED);
				out.writeInt(id);
				out.writeInt((int) player.locX());
				out.writeInt((int) player.locY());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	void send_chat(String msg) {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.CHAT_MESSAGE);
				out.writeUTF("Player " + id + ": " + msg);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	public void send_flame(double speed, double angle) {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.FLAME_FIRED);
				out.writeInt(id);
				out.writeDouble(player.locX());
				out.writeDouble(player.locY());
				out.writeDouble(speed);
				out.writeDouble(angle);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	public void send_bullet_fired(int radius, double speed, double angle) {
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.BULLET_FIRED);
				out.writeInt(id);
				out.writeDouble(player.locX());
				out.writeDouble(player.locY());
				out.writeInt(radius);
				out.writeDouble(speed);
				out.writeDouble(angle);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	private void send_item_picked_up(PVector loc) {
		int x = (int) loc.x >> Map.LOG;
		int y = (int) loc.y >> Map.LOG;
		try {
			synchronized (OUT_LOCK) {
				out.writeByte(Protocol.ITEM_PICKED_UP);
				out.writeInt(x);
				out.writeInt(y);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			appendError(e.getMessage());
		}
	}
	
	
	

	private class InputHandler implements Runnable {
		private final DataInputStream in;

		public InputHandler(DataInputStream i) {
			in = i;
		}

		public void run() {
			while (true) {
				try {
					handle_input(in.readByte());
				} catch (IOException e) {
					e.printStackTrace();
					appendError("Failed to read server message.\nDisconnected from server.", e);
					break;
				}
			}
		}
		
		private void handle_input(final byte type) throws IOException {
			switch (type) {
			
			case Protocol.PLAYER_MOVED:
				set_loc(in.readInt(), in.readInt(), in.readInt());
				break;
				
			case Protocol.BULLET_FIRED:
				handle_bullet_fired();
				break;
				
			case Protocol.PLAYER_CONNECTED:
				handle_player_connected();
				break;
				
			case Protocol.PLAYER_DISCONNECTED:
				handle_player_disconnected();
				break;
				
			case Protocol.CHAT_MESSAGE:
				chat.append_feed(in.readUTF());
				break;
				
			case Protocol.IN_INTERMISSION:
				handle_in_intermission();
				break;
				
			case Protocol.FLAME_FIRED:
				handle_flame_fired();
				break;
				
			case Protocol.ITEM_PICKED_UP:
				synchronized (REMOVED_ITEMS_LOCK) {
					removed_items.add(new int[] { in.readInt(), in.readInt() });
				}
				break;
				
			case Protocol.HEALTH_UPDATED:
				handle_health_updated();
				break;
				
			case Protocol.PLAYER_RESPAWNED:
				int id = in.readInt();
				set_loc(id, in.readInt(), in.readInt());
				players_health[id] = 1;
				break;
			}
		}
		
		private void handle_in_intermission() throws IOException {
			in_intermission = true;
			current_map_id = in.readInt();
			intermission_scoreboard = new ArrayList<String>();
			int length = in.readInt();
			for (int i = 0; i < length; i++)
				intermission_scoreboard.add(in.readUTF());
		}
		
		private void handle_flame_fired() throws IOException {
			int id = in.readInt();
			double x = in.readDouble();
			double y = in.readDouble();
			double speed = in.readDouble();
			double angle = in.readDouble();
			PVector p = new PVector((float) x, (float) y);
			Bullet b = new FireMP(id, p, 50, speed, angle, GameConstants.MP_FIRE_DAMAGE);
			synchronized (BULLETS_LOCK) {
				bullets.add(b);
			}
		}

		private void handle_bullet_fired() throws IOException {
			int id = in.readInt();
			double x = in.readDouble();
			double y = in.readDouble();
			int radius = in.readInt();
			double speed = in.readDouble();
			double angle = in.readDouble();
			PVector p = new PVector((float) x, (float) y);
			Bullet b = new BulletMP(Sprites.ARROW, id, p, radius, speed, angle, GameConstants.MP_BULLET_DAMAGE);
			synchronized (BULLETS_LOCK) {
				bullets.add(b);
			}
		}

		private void handle_player_connected() throws IOException {
			final int id = in.readInt();
			chat.append_feed("Player " + id + " connected.");
			players_scores[id] = 0;
			send_player_moved();
		}

		private void handle_player_disconnected() throws IOException {
			final int id = in.readInt();
			set_loc(id, 0, 0);
			players_scores[id] = -1;
			players_health[id] = 1;
			chat.append_feed("Player " + id + " disconnected.");
		}

		private void handle_health_updated() throws IOException {
			int shootee_id = in.readInt();
			int shooter_id = in.readInt();
			double health_incrmnt = in.readDouble();
			players_health[shootee_id] += health_incrmnt;
			if (players_health[shootee_id] > 1)
				players_health[shootee_id] = 1;
			if (players_health[shootee_id] <= 0) {
				players_health[shootee_id] = 1;
				players_scores[shooter_id]++;
				if (shootee_id == id) {
					spawn_player();
					Sound.playPlayerDeath();
				} else {
					Sound.playEnemyDeath();
				}
			}
		}
	}
	
	
	

	public void keyPressed(int key) {
		player.keyPressed(key);
	}

	public void keyReleased(int key) {
		player.keyReleased(key);
	}
	
	
	

	public void mousePressed(int mouseX, int mouseY) {
		if (exitButton.mouseOver(mouseX, mouseY)) {
			exitButton.press(new Function() {
				public void invoke() {
					chat.close();
					try {
						// socket will be null if failed to connect to server
						if (socket != null)
							// This will terminate this clients' ascosiated
							// ClientHandler
							socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					StateStack.reset();
					StateStack.setCurrentState(new MainMenu(Main.getInstance()));
				}
			});
		}
		
		if (scoreboard_btn.mouseOver(mouseX, mouseY)) {
			scoreboard_btn.press(new Function() {
				public void invoke() {
					showing_scoreboard = !showing_scoreboard;
				}
			});
		}
		
		super.mousePressed(mouseX, mouseY);
	}
	
	
	

	@Override
	public PVector screen_loc() {
		return new PVector(player.locX() - Main.WIDTH / 2, player.locY() - Main.HEIGHT / 2);
	}
	
	
	

	@Override
	public Map get_map() {
		return map;
	}
	
	
	
	public int getCurMapID() {
		return current_map_id;
	}
}
