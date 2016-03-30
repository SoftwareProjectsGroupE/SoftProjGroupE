package states;

import general.GameSP;
import general.Main;
import general.Sound;
import gui.Button;
import gui.Function;
import gui.Utils;
import map.CustomMapChooser;
import map.PreMapEditor;
import multiplayer.GameMP;
import processing.core.PGraphics;

public class MainMenu implements State {

	private Main m;

	private Button singlePlayerGameButton = new Button("Single Player", Main.WIDTH / 2 - 75, Main.HEIGHT - 400, 150, 50);
	private Button howToPlayButton = new Button("How to play", Main.WIDTH / 2 - 75, Main.HEIGHT - 325, 150, 50);
	private Button multiplayerGameButton = new Button("Multiplayer (BETA)", Main.WIDTH / 2 - 75, Main.HEIGHT - 250, 150, 50);
	private Button mapEditorButton = new Button("Map Editor", Main.WIDTH / 2 - 75, Main.HEIGHT - 175, 150, 50);
	private Button customMapGameButton = new Button("Custom Map Game", Main.WIDTH / 2 - 75, Main.HEIGHT - 100, 150, 50);

	public MainMenu(Main m) {
		this.m = m;
		if (!Sound.menuThemePlaying()) 
			Sound.playTheme(0);
		// stop any looping multiplayer music
		Sound.playThemeMP(-1);
	}

	public void onStart() {
	}

	public void update(Main m) {

	}

	private boolean connecting = false;

	public void render(PGraphics p) {
		if (connecting) {
			p.background(0);
			p.fill(255);
			p.text("Connecting to server...", Main.WIDTH / 2, Main.HEIGHT / 2);
			return;
		}
		p.background(Utils.pulse(1));
		
		p.textSize(40);
		p.fill(255, 0, 0);
		p.text("World Warriors", Main.WIDTH/2, 100);
		
		p.textSize(12);
		p.fill(255, 0, 0);

		singlePlayerGameButton.render(p, p.color(255, 0, 0));
		howToPlayButton.render(p, p.color(255, 0, 0));
		multiplayerGameButton.render(p, p.color(255, 0, 0));
		mapEditorButton.render(p, p.color(255, 0, 0));
		customMapGameButton.render(p, p.color(255, 0, 0));
	}

	public void onExit() {
	}

	public void keyPressed(int key) {
	}

	public void keyReleased(int key) {
	}

	public void mousePressed(int mouseX, int mouseY) {
		if (connecting)
			return;

		if (multiplayerGameButton.mouseOver(mouseX, mouseY)) {
			multiplayerGameButton.press(new Function() {
				public void invoke() {
					connecting = true;
					new Thread(new Runnable() {
						public void run() {
							StateStack.setCurrentState(new GameMP());
						}
					}).start();
				}
			});
		}

		if (singlePlayerGameButton.mouseOver(mouseX, mouseY)) {
			singlePlayerGameButton.press(new Function() {
				public void invoke() {
					StateStack.setCurrentState(new GameSP());
				}
			});
		}
		
		if (howToPlayButton.mouseOver(mouseX, mouseY)) {
			howToPlayButton.press(new Function() {
				public void invoke() {
					StateStack.setCurrentState(new HowToPlayScreen());
				}
			});
		}

		if (mapEditorButton.mouseOver(mouseX, mouseY)) {
			mapEditorButton.press(new Function() {
				public void invoke() {
					StateStack.setCurrentState(new PreMapEditor(m));
				}
			});
		}

		if (customMapGameButton.mouseOver(mouseX, mouseY)) {
			customMapGameButton.press(new Function() {
				public void invoke() {
					StateStack.setCurrentState(new CustomMapChooser());
				}
			});
		}
	}

	public void mouseDragged(int mouseX, int mouseY) {
	}
}
