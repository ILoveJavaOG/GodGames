package de.ilovejava.minigames.MapTools;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent a location
 */
public class CustomLocation implements Optionable {

	//Location which gets encapsulated
	@Getter
	private final Location location;

	//Map with extra Information
	private final HashMap<String, Object> data = new HashMap<>();

	@Getter
	//Name of the location
	private final String name;

	/**
	 * Constructor to create a location
	 *
	 * @param name(String) Name of the Location
	 * @param location(Location) Bukkit location
	 * @param data(Map) Map with object data
	 */
	public CustomLocation(String name, Location location, @NotNull Map<String, Object> data) {
		this.name = name;
		this.location = location;
		this.data.putAll(data);
	}

	/**
	 * Utility method to display the location
	 *
	 * @return String representation
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (location.getWorld() != null) {
			builder.append(name).append(" : ")
					.append(location.getWorld().getName()).append("(").append(location.getX()).append(", ").append(location.getY()).append(", ").append(location.getZ()).append(")")
					.append("\n")
					.append("Information: ").append("\n");
			if (data.size() == 0) {
				builder.append("None");
			} else {
				for (Map.Entry<String, Object> info : data.entrySet()) {
					builder.append(info.getKey()).append(": ").append(info.getValue().toString()).append("\n");
				}
				builder.setLength(builder.length()-1);
			}
		}
		return builder.toString();
	}

	/**
	 * Method to retrieve option by key
	 *
	 * @param key(String) Key to look for
	 *
	 * @return Object stored at the key. Null if no object is stored
	 */
	@Override
	@Nullable
	public Object getOption(String key) {
		return data.get(key);
	}

	@Override
	public <E extends Enum<E> & Options> Object getOption(E key) {
		return data.get(key.name());
	}

	public boolean containsData(String key) {
		return data.containsKey(key);
	}
}
