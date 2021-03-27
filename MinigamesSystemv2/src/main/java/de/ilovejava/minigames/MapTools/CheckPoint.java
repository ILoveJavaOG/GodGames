package de.ilovejava.minigames.MapTools;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Class to represent a checkpoint
 */
public class CheckPoint implements Comparable<CheckPoint> {

	//Edges for the checkpoint
	private final CustomLocation first;
	private final CustomLocation second;

	@Getter
	private final Integer number;
	/**
	 * Constructor for the checkpoint
	 *
	 * @param first(CustomLocation): First edge
	 * @param second(CustomLocation): Second edge
	 */
	public CheckPoint(CustomLocation first, CustomLocation second) {
		this.first = first;
		this.second = second;
		this.number = first.getIntOption("NUMBER");
	}

	private double square(double x) {
		return x*x;
	}

	private double lengthSquared(Location v, Location w) {
		return square(v.getX() - w.getX()) + square(v.getZ() - w.getZ());
	}

	/**
	 * Method to calculate the distance from a location to the checkpoint
	 *
	 * @param P(Location): Location to calculate data for
	 *
	 * @return Distance to location
	 */
	public double distanceSquared(Location P) {
		@NotNull Location A = first.getLocation();
		@NotNull Location B = second.getLocation();
		double lengthS = lengthSquared(A, B);
		double projection =  ((P.getX() - A.getX()) * (B.getX() - A.getX()) + (P.getZ() - A.getZ()) * (B.getZ() - A.getZ())) / lengthS;
		projection = Math.max(0, Math.min(1, projection));
		return lengthSquared(P, new Location(P.getWorld(), A.getX() + projection * (B.getX() - A.getX()), 0, A.getZ() + projection * (B.getZ() - A.getZ())));
	}

	@Override
	public int compareTo(@NotNull CheckPoint other) {
		return number.compareTo(other.number);
	}
}
