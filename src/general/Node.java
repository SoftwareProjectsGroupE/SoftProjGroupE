package general;

import processing.core.PVector;

public class Node /*implements Comparable<Node>*/ {

	private final PVector loc;
	public final Node parent;
	private double gCost, heuristic;

	public Node(Node parent, PVector tile) {
		this.parent = parent;
		this.loc = tile;
	}

	public PVector loc() {
		return loc.copy();
	}

	public double getG() {
		return gCost;
	}

	public void setG(double g) {
		gCost = g;
	}

	public void setH(double h) {
		heuristic = h;
	}

	public double getF() {
		return heuristic + gCost;
	}

	@Override
	final public boolean equals(Object o) {
		if (!(o instanceof Node))
			throw new IllegalArgumentException("'o' SHOULD be a Node");
		final Node n = (Node) o;
		return this.loc.equals(n.loc());
	}

	/*
	@Override
	public int compareTo(Node o) {}*/
}