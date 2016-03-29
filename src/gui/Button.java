package gui;

import general.Main;
import general.Sound;
import processing.core.PGraphics;

public class Button {
	
	public final int x, y, w, h;
	public final String label;
	private Function function;

	public Button(String label, int x, int y, int w, int h) {
		this.label = label;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Button set_func(Function function) {
		this.function = function;
		return this;
	}

	public void press(Function function) {
		function.invoke();
		Sound.playClick();
	}

	public void press() {
		if (function != null) {
			function.invoke();
			Sound.playClick();
		}
	}

	public boolean mouseOver(int mouseX, int mouseY) {
		return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
	}

	public void render(PGraphics p, int c) {
		p.fill(mouseOver(Main.mouseX, Main.mouseY) ? 255 : c);
		p.strokeWeight(1);
		p.stroke(0);
		p.noLights();
		p.rect(x, y, w, h);
		p.fill(0);
		p.textSize(12);
		p.text(label, x + w / 2, y + h / 2);
	}
}
