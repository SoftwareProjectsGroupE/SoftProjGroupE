package general;


public class Controller {
	public boolean up, down, left, right;
	
	public void keyPressed(int key) {
		if (key == 'a' || key == 'A') left = true;
		if (key == 'w' || key == 'W') up = true;
		if (key == 'd' || key == 'D') right = true;
		if (key == 's' || key == 'S') down = true;
	}
	
	public void keyReleased(int key) {
		if (key == 'a' || key == 'A') left = false;
		if (key == 'w' || key == 'W') up = false;
		if (key == 'd' || key == 'D') right = false;
		if (key == 's' || key == 'S') down = false;
	}

	public int direction() {	
		if (up && right)
			return 2;
		if (right && down)
			return 5;
		if (down && left)
			return 6;
		if (left && up)
			return 3;
		if (up)
			return 1;
		if (right)
			return 0;
		if (down)
			return 4;
		if (left)
			return 7;
		return -1;
	}

	public void reset() {
		up = down = left = right = false;
	}
}
