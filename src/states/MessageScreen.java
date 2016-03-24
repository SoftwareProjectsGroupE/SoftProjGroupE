package states;

import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;

public class MessageScreen implements State {

	private final String MESSAGE;

	private Button backButton = new Button("Back", Main.WIDTH - 100, 0, 100, 25);
	private Function func = new Function() {};

	public MessageScreen(String msg) {
		MESSAGE = msg;
	}

	public MessageScreen(String msg, Function f) {
		MESSAGE = msg;
		func = f;
	}

	public void onStart() {

	}

	public void update(Main m) {

	}

	public void render(PGraphics p) {
		p.background(0);
		p.fill(255);
		p.text(MESSAGE, Main.WIDTH / 2, Main.HEIGHT / 2);
		backButton.render(p, p.color(255, 0, 0));
	}

	public void onExit() {

	}

	public void keyPressed(int key) {

	}

	public void keyReleased(int key) {

	}

	public void mousePressed(int mouseX, int mouseY) {
		if (backButton.mouseOver(mouseX, mouseY)) {
			backButton.press(func);
		}
	}

	public void mouseDragged(int mouseX, int mouseY) {

	}
}
