package de.ilovejava.minigames.MapTools;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class Position implements Comparable<Position> {

	private Integer lab = 0;

	@Setter
	private Double distance = 0.0;

	@Getter
	private final Entity identifier;

	private CheckPoint currentCheckpoint;

	@Getter
	private CheckPoint nextCheckpoint;

	public Position(Entity identifier, CheckPoint initial) {
		this.identifier = identifier;
		this.currentCheckpoint = initial;
		this.nextCheckpoint = initial;

	}

	@Override
	public int compareTo(@NotNull Position other) {
		int comparisonLab = lab.compareTo(other.lab);
		if (comparisonLab == 0) {
			int comparisonCheckpoint = currentCheckpoint.compareTo(other.currentCheckpoint);
			if (comparisonCheckpoint == 0) {
				return distance.compareTo(other.distance);
			} else {
				return comparisonCheckpoint;
			}
		} else {
			return comparisonLab;
		}
	}

	public void update(CheckPoint newNextCheckpoint) {
		this.currentCheckpoint = nextCheckpoint;
		this.nextCheckpoint = newNextCheckpoint;
	}

	public void increaseLab() {
		lab += 1;
	}
}
