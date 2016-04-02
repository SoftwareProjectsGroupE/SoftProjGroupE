package states;

import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;

public class HowToPlayScreen implements State {
	
	private Button back_btn = new Button("Back", Main.WIDTH - 100, 0, 100, 25);

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
		p.textSize(14);
		p.text("Single player:\n"
				+ "Your objective is to make it through all of the ten levels without dying.\n"
				+ "You must kill every enemy to complete a level."
				+ "\nCollect weapons and upgrades to assist you throughout your mission."
				+ "\nGood luck!"
				+ "\n"
				+ "\nKey Controlls:"
				+ "\nw = up"
				+ "\ns = down"
				+ "\na = left"
				+ "\nd = right"
				+ "\nmouse click/hold = shoot"
				+ "\nmouse movement = aim"
				+ "\nFor the best gaming experience it is reccomended that you use a usb mouse (not a track pad)"
				+ "\n\nMultiplayer:\n"
				+ "(Multiplayer is currently in development and is not yet fully featured. This is a beta version)\n"
				+ "Kill other players. First person to 20 kills wins the match."
				+ "\nPlease read the 'READ ME.txt' file for instructions on connecting to multiplayer."
				+ "", Main.WIDTH/2, Main.HEIGHT/2);
		back_btn.render(p, p.color(255, 0, 0));

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
		if (back_btn.mouseOver(mouseX, mouseY)) 
			back_btn.press(new Function(){});
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

}
