package states;

import general.Main;
import processing.core.PGraphics;

public class EmptyState implements State {
	public void onStart() {}
	public void update(Main m) {}
	public void render(PGraphics p) {
		p.background(0);
		p.fill(255);
		p.text("......", Main.WIDTH/2, Main.HEIGHT/2);
	}
	public void onExit() {}
	public void keyPressed(int key) {}
	public void keyReleased(int key) {}
	public void mousePressed(int mouseX, int mouseY) {}
	public void mouseDragged(int mouseX, int mouseY) {}
}

