package gui;

import general.Main;
import states.MainMenu;
import states.StateStack;

public interface Function {
	default void invoke() {
		StateStack.reset();
		StateStack.push(new MainMenu(Main.getInstance()));
	}
}
