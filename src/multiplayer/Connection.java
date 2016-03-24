package multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * IO Object for handling client connections to the server.
 * Messages are read from the client and echoed to all
 * other clients by the Server object.
 */

public class Connection implements Runnable {
	
	private final Server server;

	private DataInputStream in;
	private DataOutputStream out;

	/**
	 * Unique identifier for this connection. Also represents the index 
	 * which this connection is to be stored at in the connections array.
	 * */
	private final int id;
	
	private boolean removed = false;

	
	
	
	public Connection(Server server, Socket s, int id) {
		this.server = server;
		this.id = id;
		
		try {
			out = new DataOutputStream(s.getOutputStream());
			out.flush();
			in = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Error setting up client IO streams.");
			e.printStackTrace();
		}
		
		new Thread(this, "Connection thread " + id).start();
	}
	
	


	public void remove() {
		removed = true;
	}
	
	
	
	
	public void run() {
		// send current game's scoreboard, player healths, items removed etc...
		try {
			send_game_state();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to send current game state " + e.getMessage());
			remove_Connection();
			return;
		}
		
		// notify all players in current game that this new player has connected
		try {
			server.echo_player_connected(id);
		} catch (IOException e) {
			e.printStackTrace();
			remove_Connection();
			return;
		}
		
		while (!removed) {
			try {
				handle_input(in.readByte());
			} catch (IOException e) {
				// this happens when a player disconnects
				e.printStackTrace();
				break;
			}
		}
		
		remove_Connection();
	}
	
	
	
	
	private void send_game_state() throws IOException {
		out.writeInt(id);
		out.writeInt(server.get_current_map_id());
		out.writeLong(server.elapsed_intermission_time());
		
		for (int i = 0; i < Protocol.MAX_PLAYERS; i++) {
			synchronized (server.CONNECTIONS_LOCK) {
				out.writeInt(server.connections[i] == null ? -1 : server.players_scores[i]);
			}
			out.writeDouble(server.players_health[i]);
		}
		
		final List<String> scores = server.get_scores();
		out.writeInt(scores.size());
		for (String s : scores)
			out.writeUTF(s);
		
		synchronized (server.REMOVED_ITEMS_LOCK) {
			out.writeInt(server.removed_items.size());
			for (int[] a : server.removed_items) {
				out.writeInt(a[0]);
				out.writeInt(a[1]);
			}
		}
		
		out.flush();
	}
	
	
	

	private void handle_input (final byte message_type) throws IOException {
		switch (message_type) {
			case Protocol.PLAYER_MOVED:
				server.echo_player_moved(in.readInt(), in.readInt(), in.readInt());
				break;
				
			case Protocol.BULLET_FIRED:
				server.echo_bullet_fired(in.readInt(), in.readDouble(), in.readDouble(), 
						                 in.readInt(), in.readDouble(), in.readDouble());
				break;	
				
			case Protocol.CHAT_MESSAGE:
				server.echo_chat(in.readUTF());
				break;	
				
			case Protocol.FLAME_FIRED:
				server.echo_flame_fired(in.readInt(), in.readDouble(), in.readDouble(), 
						                              in.readDouble(), in.readDouble());
				break;	
				
			case Protocol.ITEM_PICKED_UP:
				server.echo_item_picked_up(in.readInt(), in.readInt());
				break;	
				
			case Protocol.HEALTH_UPDATED:
				server.echo_health_updated(in.readInt(), in.readInt(), in.readDouble());
				break;	
				
			case Protocol.PLAYER_RESPAWNED:
				server.echo_player_respawned(in.readInt(), in.readInt(), in.readInt());
				break;
		}
	}
	
	
	

	void remove_Connection() {
		synchronized(server.CONNECTIONS_LOCK) { 
			server.connections[id] = null; 
		
			if (!server.in_intermission()) {
				 server.players_scores[id] = -1;
				 server.players_health[id] = 1;
			}
		}
		
		server.append_feed("Player " + id + " has disconnected.");
		
		if (server.all_disconnected())
			server.append_feed("Game lobby is empty.");

		server.echo_player_disconnected(id);

		try {
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public DataOutputStream out_strm() {
		return out;
	}
}
