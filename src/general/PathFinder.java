package general;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import map.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class PathFinder {
	
	private final boolean DIAG_MOVEMENT_ENABLED;
	
	public PathFinder(boolean b) {
		DIAG_MOVEMENT_ENABLED = b;
	}

	private final Comparator<Node> node_comparator = new Comparator<Node>() {
		public int compare(Node n1, Node n2) {
			if (n1.getF() > n2.getF())
				return 1;
			if (n1.getF() < n2.getF())
				return -1;
			return 0;
		}
	};

	public List<Node> a_star_search(Map map, PVector start, PVector target) {

		final Queue<Node> open = new PriorityQueue<Node>(11, node_comparator);
		final Set<PVector> visited = new HashSet<PVector>();

		Node origin = new Node(null, start);
		origin.setH(PVector.dist(start, target));

		open.add(origin);

		while (!open.isEmpty()) {

			Node current = open.poll();

			if (equal(current.loc(), target))
				return backtracePath(current);

			visited.add(current.loc());

			for (Node n : neighbours(current)) {

				if (!map.walkable(n))
					continue;

				n.setG(current.getG() + PVector.dist(current.loc(), n.loc()));
				n.setH(PVector.dist(n.loc(), target));

				if (visited.contains(n.loc()) && n.getF() >= current.getF())
					continue;

				if (!open.contains(n) || n.getF() < current.getF())
					open.add(n);
			}
		}
		return null;
	}

	private List<Node> neighbours(Node current) {
		final List<Node> neighbours = new ArrayList<Node>();
		
		for (int i = 0; i < 9; i++) {

			if (DIAG_MOVEMENT_ENABLED) {
				if (i == 4)
					continue;
			} else {
				if (i % 2 == 0)
					continue;
			}

			int x = (i % 3) - 1;
			int y = (i / 3) - 1;
			PVector p = new PVector(x + (int) current.loc().x, y + (int) current.loc().y);
			Node n = new Node(current, p);
			neighbours.add(n);
		}
		return neighbours;
	}

	private List<Node> backtracePath(Node current) {
		List<Node> path = new ArrayList<Node>();
		while (current.parent != null) {
			path.add(current);
			current = current.parent;
		}
		return path;
	}

	public void drawPath(PApplet p, List<Node> path) {
		int bs = Map.BLOCK_SIZE;
		for (int i = 0; i < path.size() - 1; i++) {
			PVector v = path.get(i).loc();
			PVector v2 = path.get(i + 1).loc();
			p.stroke(255, 0, 0);
			p.strokeWeight(5);
			p.line(v.x * bs + bs / 2, v.y * bs + bs / 2, v2.x * bs + bs / 2, v2.y * bs + bs / 2);
			p.stroke(0);
			p.strokeWeight(1);
		}
	}

	private boolean equal(PVector v1, PVector v2) {
		return (int) v1.x == (int) v2.x && (int) v1.y == (int) v2.y;
	}
}
