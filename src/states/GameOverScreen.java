package states;

import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;

public class GameOverScreen implements State {

	private Button mainMenuBtn = new Button("Main menu", Main.WIDTH / 2 - 75, Main.HEIGHT / 2 - 25, 150, 50);

	public void onStart() {

	}

	public void render(PGraphics p) {
		p.fill(0, 3);
		p.rect(0, 0, Main.WIDTH, Main.HEIGHT);
		mainMenuBtn.render(p, p.color(255, 0, 0));
		p.fill(255,0,0);
		p.textSize(40);
		p.text("YOU DIED\nGAME OVER", Main.WIDTH / 2, Main.HEIGHT / 2 - 200);
		p.textSize(12);
	}

	public void onExit() {

	}

	public void keyPressed(int key) {

	}

	public void keyReleased(int key) {

	}

	public void mousePressed(int mouseX, int mouseY) {
		if (mainMenuBtn.mouseOver(mouseX, mouseY)) {
			mainMenuBtn.press(new Function() {
			});
		}
	}

	public void mouseDragged(int mouseX, int mouseY) {

	}

	@Override
	public void update(Main m) {

	}

}
