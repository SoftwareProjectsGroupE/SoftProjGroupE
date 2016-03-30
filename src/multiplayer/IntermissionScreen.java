package multiplayer;

import java.util.List;

import general.Main;
import general.Sound;
import gui.Button;
import processing.core.PGraphics;
import states.State;
import states.StateStack;

public class IntermissionScreen implements State {

	private Button exitLobbyButton = new Button("Exit lobby", Main.WIDTH - 100, 0, 100, 25);

	private GameMP game_client;

	private final List<String> scores;
	private long timeout;

	public IntermissionScreen(GameMP game_client, List<String> scores, long elapsed_intermission_time) {
		this.game_client = game_client;
		this.scores = scores;
		timeout = System.currentTimeMillis() + Protocol.INTERMISSION_TIME;
		if (elapsed_intermission_time != -1)
			timeout -= elapsed_intermission_time;
	}

	@Override
	public void onStart() {}

	@Override
	public void update(Main m) {
		if (System.currentTimeMillis() > timeout) {
			StateStack.pop();
			Sound.playThemeMP(game_client.getCurMapID());
		}
	}

	@Override
	public void render(PGraphics buffer) {
		buffer.background(0);
		buffer.text("Next match in :" + ((timeout - System.currentTimeMillis())/1000 + " seconds"), 100, 100);
		buffer.textSize(30);
		buffer.text("Intermission", Main.WIDTH/2, 60);
		buffer.textSize(15);
		String scoreboard = "Kill Scoreboard\n\n";
		for (int i = scores.size() - 1; i >= 0; i--)
			scoreboard += scores.get(i) + "\n";
			
		buffer.text(scoreboard, Main.WIDTH / 2, Main.HEIGHT / 2);
		buffer.textSize(12);
		exitLobbyButton.render(buffer, buffer.color(255, 0, 0));
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		if (exitLobbyButton.mouseOver(mouseX, mouseY))
			game_client.mousePressed(mouseX, mouseY);
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

}
