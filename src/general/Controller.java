package general;


public class Controller {
	public boolean up, down, left, right;
	
	private int dir = 0;
	
	public void keyPressed(int key) {
		if (key == 'a' || key == 'A') left = true;
		if (key == 'w' || key == 'W') up = true;
		if (key == 'd' || key == 'D') right = true;
		if (key == 's' || key == 'S') down = true;
		
		if (up && right)
			dir = 2;
		else if (right && down)
			dir = 5;
		else if (down && left)
			dir = 6;
		else if (left && up)
			dir = 3;
		else if (up)
			dir = 1;
		else if (right)
			dir = 0;
		else if (down)
			dir = 4;
		else if (left)
			dir = 7;
		//else 
			//dir = -1;
	}
	
	public void keyReleased(int key) {
		if (key == 'a' || key == 'A') left = false;
		if (key == 'w' || key == 'W') up = false;
		if (key == 'd' || key == 'D') right = false;
		if (key == 's' || key == 'S') down = false;
	}

	public int direction() {
		return dir;
	}
}
