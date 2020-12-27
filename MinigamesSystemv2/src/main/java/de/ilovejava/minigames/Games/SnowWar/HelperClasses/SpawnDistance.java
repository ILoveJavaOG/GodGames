package de.ilovejava.minigames.Games.SnowWar.HelperClasses;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class SpawnDistance implements Comparable<SpawnDistance> {

	@Getter
	private final Double distance;

	@Getter
	private final Location spawn;

	public SpawnDistance(Location spawn, Double distance) {
		this.spawn = spawn;
		this.distance = distance;
	}

	@Override
	public int compareTo(@NotNull SpawnDistance o) {
		return distance.compareTo(o.distance);
	}
}
