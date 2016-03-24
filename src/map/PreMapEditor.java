package map;

import java.util.List;

import general.Main;
import gui.Button;
import gui.Function;
import processing.core.PGraphics;
import states.State;
import states.StateStack;

public class PreMapEditor implements State {

	private Main m;

	private Button blankMapButton = new Button("Blank Map", Main.WIDTH - Main.WIDTH/4 - 50, Main.HEIGHT/2 - 25, 100, 50);

	private List<String> cstm_map_files = MapFactory.getCustomMapFiles();
	private List<String> dev_map_files = MapFactory.getDevMapFiles();
	private Button[] map_buttons = new Button[cstm_map_files.size() + dev_map_files.size()];

	public PreMapEditor(Main m) {
		this.m = m;
		
		int cl = cstm_map_files.size();
		int dl = dev_map_files.size();
		int ml = map_buttons.length;
		
		for (int i = 0; i < cl; i++)
			map_buttons[i] = new Button(cstm_map_files.get(i), 50, 25 * (i - ml / 2) + Main.HEIGHT / 2, 400, 25);
		
		for (int i = 0; i < dl; i++)	
			map_buttons[i + cl] = new Button(dev_map_files.get(i), 50, 25 * (cl - ml / 2 + i) + Main.HEIGHT / 2, 400, 25);
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

		for (Button b : map_buttons)
			b.render(p, p.color(255, 0, 0));

		blankMapButton.render(p, p.color(255, 0, 0));

		p.fill(255);
		p.text("Edit an existing map or a blank map.", Main.WIDTH / 2, 100);

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
		if (blankMapButton.mouseOver(mouseX, mouseY)) {
			blankMapButton.press(new Function() {
				public void invoke() {
					StateStack.setCurrentState(new MapEditor(m));
				}
			});
		}
		for (Button b : map_buttons) {
			if (b.mouseOver(mouseX, mouseY)) {
				b.press(new Function() {
					public void invoke() {
						MapEditor me = new MapEditor(m, new Map(b.label)); 
						StateStack.setCurrentState(me);
					}
				});
			}
		}
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {}
}
