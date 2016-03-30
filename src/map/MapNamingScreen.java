package map;

import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PApplet;
import processing.core.PGraphics;
import states.State;
import states.StateStack;

public class MapNamingScreen implements State {

	private MapEditor mapEditor;

	private Button doneButton = new Button("Done", Main.WIDTH / 2 - 50, Main.HEIGHT / 2 + 75, 100, 50);

	private String name = "";

	public MapNamingScreen(MapEditor me) {
		mapEditor = me;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Main m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(PGraphics p) {
		p.background(0);
		p.fill(255);
		p.text("Please enter below, a name for the map file.", Main.WIDTH / 2, Main.HEIGHT / 2 - 100);
		p.fill(255);
		p.rectMode(PApplet.CENTER);
		p.rect(Main.WIDTH / 2, Main.HEIGHT / 2, Main.WIDTH - 200, 50);
		p.rectMode(PApplet.CORNER);
		p.fill(0);
		p.text(name, Main.WIDTH / 2, Main.HEIGHT / 2);
		doneButton.render(p, p.color(255, 0, 0));

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key) {
		if (name.length() > 50) return;
		if (key == 8) {
			if (name.length() > 0)
				name = name.substring(0, name.length() - 1);
		} else {
			if (String.valueOf((char) key).matches("[0-9a-zA-Z]+"))
				name += (char) key;
		}
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		if (doneButton.mouseOver(mouseX, mouseY)) {
			doneButton.press(new Function() {
				public void invoke() {
					StateStack.pop();
					mapEditor.saveAsFile(name);
				}
			});
		}

	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

}
