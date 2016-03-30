package multiplayer;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


/*
ONLY ONE LOCAL SERVER CAN BE RUNNING PER LOCAL NETWORK
ONLY ONE PUBLIC SERVER CAN BE RUNNING GLOBALLY
CLIENTS WILL CHECK LOCALLY FIRST FOR HOSTS TO CONNECT TO. THEN GLOBALLY IF NO LOCAL HOSTS ARE AVAILABLE.

FOUR POSSIBLE SERVER STATES:
NOT RUNNING 
LISTENING LOCAL
LISTENING PUBLIC
LISTENING LOCAL AND PUBLIC
*/

public class Server {

	private final JFrame frame = new JFrame();
	private final JTextArea feed = new JTextArea();

	private final String LOCAL_IP;
	private final String PUBLIC_IP;

	private final boolean LISTENING_PUBLIC;
	private final boolean LISTENING_LOCAL;

	private boolean server_started = false;
	
	private int current_map_id;

	private long intermission_start = -1;
	private long intermission_end;
		
	private final int MAX_KILLS = 20;// match ends when a player gets 20 kills

	// connections must be synchronized or multiple outputstreams will be writing to the sockets outputstream at once
	public Connection[] connections = new Connection[Protocol.MAX_PLAYERS];
	public final Object CONNECTIONS_LOCK = new Object();
	
	List<int[]> removed_items = new ArrayList<int[]>();
	public final Object REMOVED_ITEMS_LOCK = new Object();
	
	double[] players_health = new double[Protocol.MAX_PLAYERS];
	int[] players_scores = new int[Protocol.MAX_PLAYERS];
	
	
	
	
	public Server() throws IOException {
		setup_frame();
		
		set_current_map();

		LOCAL_IP  = try_get_localIP();
		PUBLIC_IP = try_get_publicIP();

		append_feed("Checking for already-active servers...");
		
		LISTENING_LOCAL  = LOCAL_IP != null &&
				           !server_already_active("local") &&
				           try_save_to_database("set_local_host=" + LOCAL_IP);
		LISTENING_PUBLIC = PUBLIC_IP != null &&
			               !server_already_active("public") &&
			               try_save_to_database("set_public_host=" + PUBLIC_IP);

		if (!LISTENING_PUBLIC && !LISTENING_LOCAL)
			return;

		server_started = true;

		add_shutdown_hook();
		
		ServerSocket server_socket = try_setup_server_socket();
		if (server_socket == null)
			return;
		
		print_listening_state();
		
		run_listener(server_socket);

		manage_games();
	}
	
	
	
	
	private void set_current_map() {
		current_map_id = (int) (Math.random() * Protocol.MAP_COUNT); 
		append_feed("Random map ID chosen: " + current_map_id);
	}
	

	

	private boolean try_save_to_database(String arg) {
		ArrayList<String> result = http_request(arg);
		return result != null && 
		(result.get(0).equals("host_set") || result.get(0).equals("host_already_set"));
	}
	
	
	

	private boolean server_already_active(String type) {
		ArrayList<String> ips = type.equals("local") ? 
				                http_request("get_potential_local_hosts") : 
					            http_request("get_potential_public_host");
		if (ips == null)
			return true;
		
		if (ips.get(0).equals("no_results"))
			return false;
		
		for (String ip : ips) {
			String hiddenIP = ip.substring(0, 5) + "...";
			try {		
				append_feed("Checking if " + hiddenIP + " is a potential host...");
				Socket s = new Socket();
				s.connect(new InetSocketAddress(ip, Protocol.PORT), 2000);
				s.close();
				if (ip.equals(LOCAL_IP) || ip.equals(PUBLIC_IP)) {
					append_feed("You can't start the server more than once!");
					return true;
				}
				append_feed("Unable to start " + type + " server.\n" + type + " server is already running at:\n    " + hiddenIP);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				append_feed(hiddenIP + " is inactive.");
			}
		}
		return false;
	}
	
	
	

	private ArrayList<String> http_request(String arg) {
		
		ArrayList<String> response = new ArrayList<String>();
		String line = null;
		
		try {
			URL igor = new URL(Protocol.SCRIPT_URL + arg);
			URLConnection con = igor.openConnection();
			
			InputStreamReader is = new InputStreamReader(con.getInputStream());
			BufferedReader br = new BufferedReader(is);
			
			while ((line = br.readLine()) != null) 
				if (line.trim().matches("[0-9\\.]+|[_a-z]+")) 
					response.add(line.trim());
					
		} catch (IOException e) {
			e.printStackTrace();
			append_feed("Failed to run remote script for arg: " + arg);
			return null;
		}
		if (response.size() == 0) {
			append_feed("Remote script returned empty response for arg: " + arg);
			return null;
		}
		return response;
	}
	
	
	

	private String try_get_localIP() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			append_feed("Found your local IP:\n    " + ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			append_feed("Failed to get local IP of this machine.");
		}
		return ip;
	}

	
	
	
	private String try_get_publicIP() {
		String ip = null;
		try {
			URL ip_grabber = new URL(Protocol.IP_LOOKUP_URL);
			BufferedReader in = new BufferedReader(new InputStreamReader(ip_grabber.openStream()));
			ip = in.readLine();
			append_feed("Found your public IP:\n    " + ip);
		} catch (IOException e) {
			e.printStackTrace();
			append_feed("Failed to get public IP of this machine.");
		}
		return ip;
	}
	
	
	
	
	private void add_shutdown_hook() {
		// if program is closed externally, ensure endsession is still run
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				end_session();
			}
		});
	}
	

	

	private void print_listening_state() {
		String ip = "";
		if (LISTENING_LOCAL && !LISTENING_PUBLIC)
			ip = LOCAL_IP;
		
		else if (LISTENING_PUBLIC && !LISTENING_LOCAL)
			ip = PUBLIC_IP;
		
		else if (LISTENING_LOCAL && LISTENING_PUBLIC)
			ip = LOCAL_IP + "\nand hosting game globally at:\n    " + PUBLIC_IP;
		
		append_feed("Server started. Hosting game locally at:\n    " + ip + "\nlistening on port: " + Protocol.PORT + "...");
	}
	
	
	
	
	private ServerSocket try_setup_server_socket() {
		append_feed("Starting server...");
		ServerSocket server_socket = null;
		try {
			server_socket = new ServerSocket(Protocol.PORT);
		} catch (IOException e) {
			append_feed("Unable to start server. \nPort " + Protocol.PORT + " is already in use by another application?");
			e.printStackTrace();
		}
		return server_socket;
	}
	
	
	
	
	private void manage_games() {
		Arrays.fill(players_scores, -1);
		Arrays.fill(players_health, 1);

		while (true) {
			if (intermission_ended() || all_disconnected()) {
				players_scores = new int[Protocol.MAX_PLAYERS];
				players_health = new double[Protocol.MAX_PLAYERS];
				Arrays.fill(players_health, 1);
				
				synchronized (REMOVED_ITEMS_LOCK) {
					removed_items = new ArrayList<int[]>();
				}
				
				intermission_start = -1;
			}
			
			if (intermission_start == -1) {
				for (int i : players_scores) {
					if (i >= MAX_KILLS) {
						echo_intermission_started();
						break;
					}
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	private void run_listener(ServerSocket serverSocket) throws IOException {
		Runnable r = new Runnable() {
			public void run() {
				try {
					// always listen for new clients
					while (true) {
						// accept() blocks until it recieves a client connection. It
						// listens on the specified port.
						Socket s = serverSocket.accept();
						if (max_players_reached()) {
							// close the connection instantly
							s.close();
							continue;
						}
						add_connection(s);
						append_feed("Player connected: " + s.getInetAddress().toString().substring(0, 9) + "...");
					}
				} catch (IOException e) {
					e.printStackTrace();
					append_feed("Unable to accept client.");
				} finally {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					end_session();
				}
			}
		};
		new Thread(r, "Server listening thread").start();
	}
	
	
	
	
	private void add_connection(Socket s) {
		synchronized(CONNECTIONS_LOCK) {
			for (int i = 0; i < connections.length; i++) {
				if (connections[i] == null) {
					connections[i] = new Connection(this, s, i);
					players_scores[i] = 0;
					break;
				}
			}
		}
	}
	
	
	
	
	private void end_session() {
		String result = "";
		
		if (LISTENING_LOCAL && !LISTENING_PUBLIC) 
			result += http_request("end_local_session=" + "\"" + LOCAL_IP + "\"");

		else if (!LISTENING_LOCAL && LISTENING_PUBLIC)
			result += http_request("end_public_session");

		else if (LISTENING_LOCAL && LISTENING_PUBLIC) {
			result += http_request("end_local_session=" + "\"" + LOCAL_IP + "\"") + "\n";
			result += http_request("end_public_session");
		}
		
		System.out.println(result);
	}
	
	
	
	
	long elapsed_intermission_time() {
		long elapsed_time = System.currentTimeMillis() - intermission_start;
		if (elapsed_time > Protocol.INTERMISSION_TIME)
			return -1;
		return elapsed_time;
	}
	
	
	
	
	boolean in_intermission() {
		return elapsed_intermission_time() != -1;
	}
	
	
	
	
	private boolean intermission_ended() {
		return intermission_start != -1 && 
		System.currentTimeMillis() > intermission_end;
	}
	
	
	
	
	private void echo_intermission_started() {
		
		set_current_map();
		final List<String> scores = get_scores();
		
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					try {
						out.writeByte(Protocol.IN_INTERMISSION);
						out.writeInt(current_map_id);

						out.writeInt(scores.size());
						for (String s : scores)
							out.writeUTF(s);
						
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
						c.remove();
						continue;
					}
				}
			}
		}
		intermission_start = System.currentTimeMillis();
		intermission_end = intermission_start + Protocol.INTERMISSION_TIME;
	}
	
	
	
	
	private final Comparator<Object[]> score_sorter = new Comparator<Object[]>() {
		public int compare(Object[] o1, Object[] o2) {
			return (int) o1[1] - (int) o2[1];	
		}
	};
	
	List<String> get_scores() {
		List<Object[]> holder = new ArrayList<Object[]>();
		
		synchronized (CONNECTIONS_LOCK) {
			for (int i = 0; i < Protocol.MAX_PLAYERS; i++) {
				if (connections[i] == null) 
					continue;
					
				String player = "Player " + i + ": ";
				int score = players_scores[i];
				holder.add(new Object[]{player, score});
			}
		}
		
		holder.sort(score_sorter);
		
		List<String> strings = new ArrayList<String>();
		for (Object[] obj : holder)
			strings.add((String) obj[0] + (int) obj[1]);
		
		return strings;
	}
	

	
	
	public void echo_player_respawned(int id, int x, int y) throws IOException {
		players_health[id] = 1;
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.PLAYER_RESPAWNED);
					out.writeInt(id);
					out.writeInt(x);
					out.writeInt(y);
					out.flush();
				}
			}
		}
	}
	
	
		
	
	public void echo_health_updated(int shootee_id, int shooter_id, double health_incrmnt) throws IOException {
		
		players_health[shootee_id] += health_incrmnt;
		
		if (players_health[shootee_id] > 1) 
		    players_health[shootee_id] = 1;
		
		if (players_health[shootee_id] <= 0) {
			players_scores[shooter_id]++;
			players_health[shootee_id] = 1;
		}
		
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();				
					out.writeByte(Protocol.HEALTH_UPDATED);
					out.writeInt(shootee_id);
					out.writeInt(shooter_id);
					out.writeDouble(health_incrmnt);
					out.flush();
				}
			}
		}
	}
	
	
	
	
	public void echo_item_picked_up(int x, int y) throws IOException {
		
		synchronized (REMOVED_ITEMS_LOCK) {
			removed_items.add(new int[]{x, y});
		}
		
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.ITEM_PICKED_UP);
					out.writeInt(x);
					out.writeInt(y);
					out.flush();
				}
			}
		}
	}
	
	
	

	public void echo_flame_fired(int id, double x, double y, double speed, double angle) throws IOException {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.FLAME_FIRED);
					out.writeInt(id);
					out.writeDouble(x);
					out.writeDouble(y);
					out.writeDouble(speed);
					out.writeDouble(angle);
					out.flush();
				}
			}
		}
	}
	
	
	
	
	public void echo_chat(String msg) throws IOException {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.CHAT_MESSAGE);
					out.writeUTF(msg);
					out.flush();
				}
			}
		}
	}
	
	
	

	public void echo_player_connected(int id) throws IOException {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.PLAYER_CONNECTED);
					out.writeInt(id);
					out.flush();
				}
			}
		}
	}
	
	
	
	
	public void echo_player_disconnected(int id) {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					try {
						out.writeByte(Protocol.PLAYER_DISCONNECTED);
						out.writeInt(id);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
						c.remove();
						continue;
					}				
				}
			}
		}	
	}
	
	
	

	public void echo_bullet_fired(int id, double x, double y, int radius, double speed, double angle) throws IOException {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.BULLET_FIRED);
					out.writeInt(id);
					out.writeDouble(x);
					out.writeDouble(y);
					out.writeInt(radius);
					out.writeDouble(speed);
					out.writeDouble(angle);
					out.flush();
				}
			}
		}
	}
	
	
	

	public void echo_player_moved(int id, int x, int y) throws IOException {
		synchronized (CONNECTIONS_LOCK) {
			for (Connection c : connections) {
				if (c != null) {
					DataOutputStream out = c.out_strm();
					out.writeByte(Protocol.PLAYER_MOVED);
					out.writeInt(id);
					out.writeInt(x);
					out.writeInt(y);
					out.flush();
				}
			}
		}
	}

	

	
	private boolean max_players_reached() {
		for (int i = 0; i < connections.length; i++)
			if (connections[i] == null)
				return false;
		return true;
	}
	
	

	
	boolean all_disconnected() {
		synchronized(CONNECTIONS_LOCK) {
			for (int i = 0; i < connections.length; i++)
				if (connections[i] != null)
					return false;
		}
		return true;
	}
	
	
	

	private void setup_frame() {
		frame.setTitle("Server");
		frame.add(new JScrollPane(feed), BorderLayout.CENTER);
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				if(all_disconnected()) {
					if(server_started)
						end_session();
					System.exit(0);
				} else {
					append_feed("Cannot end host while\nplayers are still connected!");
				}
			}
			public void windowClosed(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		feed.setEditable(false);
		append_feed("Warning: you will not be able to close\nthis window while players are still connected.\n");
	}
	
	
	

	// needs to be synchronized as window listener thread calls this method also
	public synchronized void append_feed(String msg) {
		System.out.println(msg);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				feed.append("> " + msg + "\n");
			}
		});
	}
	
	
	
	
	public int get_current_map_id() {
		return current_map_id;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



