package de.ilovejava.minigames.MapTools;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Class to represent a checkpoint
 */
public class CheckPoint {

	//Edges for the checkpoint
	private final CustomLocation first;
	private final CustomLocation second;

	/**
	 * Constructor for the checkpoint
	 *
	 * @param first(CustomLocation): First edge
	 * @param second(CustomLocation): Second edge
	 */
	public CheckPoint(CustomLocation first, CustomLocation second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Method to get the number of a checkpoint
	 *
	 * @return Number of the checkpoint
	 */
	public Integer getNumber() {
		return first.getData("NUMBER", Integer.class);
	}

	/**
	 * Method to calculate the distance from a location to the checkpoint
	 *
	 * @param location(Location): Location to calculate data for
	 *
	 * @return Distance to location
	 */
	public double distance(Location location) {
		@NotNull Vector f = first.getLocation().toVector();
		@NotNull Vector s = second.getLocation().toVector();
		Vector line = f.subtract(s);
		return line.distance(location.toVector());
	}
}
