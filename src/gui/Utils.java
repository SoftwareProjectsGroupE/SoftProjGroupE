package gui;

import general.Main;
import processing.core.PApplet;

public class Utils {
	public static float pulse(double rate) {
		return PApplet.map(PApplet.sin((float) (rate * PApplet.radians(Main.frameCount))), -1, 1, 0, 255);
	}
}
