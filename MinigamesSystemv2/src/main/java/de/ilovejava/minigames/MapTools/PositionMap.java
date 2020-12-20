package de.ilovejava.minigames.MapTools;

import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PositionMap {

	@Getter
	private final ArrayList<Position> positions = new ArrayList<>();

	@Getter
	private final Map<UUID, Integer> lookup = new HashMap<>();

	public void add(Position position) {
		UUID uuid = position.getIdentifier().getUniqueId();
		this.lookup.put(uuid, positions.size());
		this.positions.add(position);
	}

	public Position getPosition(Entity identifier) {
		return positions.get(lookup.get(identifier.getUniqueId()));
	}

	public void sort() {
		int size = positions.size();
		for (int i = 1; i < size; i++) {
			Position key = positions.get(i);
			int j = i - 1;
			while (j >= 0 && positions.get(j).compareTo(key) > 0) {
				positions.set(j + 1, positions.get(j));
				j--;
			}
			positions.set(j + 1, key);
		}
		for (int i = 0; i < positions.size(); i++) {
			Position position = positions.get(i);
			this.lookup.put(position.getIdentifier().getUniqueId(), i);
		}
	}

	public CheckPoint getNextCheckpoint(Entity identifier) {
		int index = lookup.get(identifier.getUniqueId());
		return positions.get(index).getNextCheckpoint();
	}

	public void update(Entity identifier, CheckPoint newNextCheckpoint) {
		int index = lookup.get(identifier.getUniqueId());
		positions.get(index).update(newNextCheckpoint);
	}
	
	public void update(Entity identifier, Position newPosition) {
		int index = lookup.get(identifier.getUniqueId());
		positions.set(index, newPosition);
	}

	public void remove(Entity boat) {
		int index = lookup.get(boat.getUniqueId());
		positions.remove(index);
		lookup.remove(boat.getUniqueId());
	}
}
