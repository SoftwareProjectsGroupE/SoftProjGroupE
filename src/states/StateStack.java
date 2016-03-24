package states;

import java.util.Vector;

public abstract class StateStack {

	private static Vector<State> stateStack = new Vector<State>();

	public static void push(State state) {
		head().onExit();
		state.onStart();
		stateStack.add(state);
	}

	public static void setCurrentState(State s) {
		if (size() > 0) {
			pop();
			push(s);
		} else {
			push(s);
		}
	}

	public static int size() {
		return stateStack.size();
	}

	public static void pop() {
		if (size() > 0) {
			head().onExit();
			stateStack.remove(size() - 1);
			head().onStart();
		}
	}

	public static void reset() {
		stateStack = new Vector<State>();
	}

	public static State head() {
		return size() > 0 ? stateStack.get(size() - 1) : new EmptyState();
	}
}
