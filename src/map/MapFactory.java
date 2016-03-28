package map;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;

public class MapFactory {

	private int currentLevel = 0;

	private static List<String> dev_map_files;
	private static List<String> cust_map_files;
	private static List<String> multiplayer_map_files;

	public static void loadMaps(PApplet p) {

		dev_map_files = find_files("/res/maps/devmaps/");

		if (dev_map_files.isEmpty())
			throw new RuntimeException("/res/maps/devmaps/ is empty!");

		cust_map_files = find_files("/res/maps/custommaps/");
		
		multiplayer_map_files = find_files("/res/maps/multmaps/");
		
		if (multiplayer_map_files.isEmpty())
			throw new RuntimeException("/res/maps/multmaps/ is empty!");
	}

	private static List<String> find_files(String path) {
		File folder = new File("." + path);
		File[] files = folder.listFiles();
		Arrays.sort(files);
		List<String> list = new ArrayList<String>();
		for (File f : files) {
			if (f.isFile()) {
				String s = f.getName();
				if (s.endsWith(".txt")) {
					list.add("." + path + s);
				}
			}
		}
		return list;
	}

	public static List<String> getDevMapFiles() {
		return dev_map_files;
	}

	public static List<String> getCustomMapFiles() {
		return cust_map_files;
	}

	public static Map firstLevel() {
		return new Map(dev_map_files.get(0));
	}

	public static Map getMap(int i) {
		if (i < 0 || i >= dev_map_files.size())
			throw new IllegalArgumentException("This map doesn't exist: " + i);
		return new Map(dev_map_files.get(i));
	}
	
	public static Map getMultMap(int i) {
		if (i < 0 || i >= multiplayer_map_files.size())
			throw new IllegalArgumentException("This map doesn't exist: " + i);
		return new Map(multiplayer_map_files.get(i));
	}

	public Map nextLevel(Map current) {
		
		// check if current is a custom map
		if (cust_map_files.contains(current.path))
			return null;

		if (++currentLevel < dev_map_files.size())
			return new Map(dev_map_files.get(currentLevel));

		return null;
	}
}
