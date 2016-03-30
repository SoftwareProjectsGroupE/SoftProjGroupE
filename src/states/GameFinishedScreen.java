package states;

import general.Main;
import general.Sound;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;

public class GameFinishedScreen implements State {

	private Button mainMenuButton = new Button("Main Menu", Main.WIDTH - 100, 0, 100, 25);

	@Override
	public void onStart() {
		Sound.playTheme(0);

	}

	@Override
	public void update(Main m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(PGraphics p) {
		p.background(0);
		p.fill(255);
		p.textSize(40);
		p.text("YOU FINISHED THE GAME!!!", Main.WIDTH / 2, Main.HEIGHT / 2);
		mainMenuButton.render(p, p.color(255, 0, 0));

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
		if (mainMenuButton.mouseOver(mouseX, mouseY)) {
			mainMenuButton.press(new Function() {});
		}

	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

}
