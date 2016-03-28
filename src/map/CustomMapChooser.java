package map;

import java.util.List;

import general.GameSP;
import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;
import states.State;
import states.StateStack;

public class CustomMapChooser implements State {

	private Button[] cstm_map_btns;
	private Button back_btn = new Button("Back", Main.WIDTH - 100, 0, 100, 25);

	public CustomMapChooser() {
		List<String> cstm_map_files = MapFactory.getCustomMapFiles();
		cstm_map_btns = new Button[cstm_map_files.size()];
		
		for (int i = 0; i < cstm_map_files.size(); i++) {
			String name = cstm_map_files.get(i);
			int x = Main.WIDTH/2 - 200;
			int y = 25 * (i - cstm_map_btns.length / 2) + Main.HEIGHT/2;
			cstm_map_btns[i] = new Button(name, x, y, 400, 25);
		}
	}

	@Override
	public void onStart() {

	}

	@Override
	public void update(Main m) {

	}

	@Override
	public void render(PGraphics p) {
		p.background(0);

		for (Button b : cstm_map_btns)
			b.render(p, p.color(255, 0, 0));

		p.fill(255);
		if (cstm_map_btns.length == 0)
			p.text("You haven't made any custom maps yet!", Main.WIDTH / 2, Main.HEIGHT/2);
		else
			p.text("Choose a custom map to play.", Main.WIDTH / 2, 100);

		back_btn.render(p, p.color(255, 0, 0));
	}

	@Override
	public void onExit() {

	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void keyReleased(int key) {

	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		for (Button b : cstm_map_btns) {
			if (b.mouseOver(mouseX, mouseY)) {
				b.press(new Function() {
					public void invoke() {
						StateStack.setCurrentState(new GameSP(new Map(b.label)));
					}
				});
			}
		}
		
		if (back_btn.mouseOver(mouseX, mouseY)) 
			back_btn.press(new Function(){});
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {

	}

}
