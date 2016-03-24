package states;

import general.Main;
import processing.core.PGraphics;

public interface State {
	void onStart();

	void update(Main m);

	void render(PGraphics buffer);

	void onExit();

	void keyPressed(int key);

	void keyReleased(int key);

	void mousePressed(int mouseX, int mouseY);

	void mouseDragged(int mouseX, int mouseY);
}
