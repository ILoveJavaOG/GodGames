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
public class CustomLocation {

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
	 * Method to retrieve option by key and cast to given class
	 *
	 * @param key(String) Key to look for
	 * @param castTo(Class) Class to cast to
	 *
	 * @return If key contains data the data will be returned as castTo class. Null if not
	 */
	@Nullable
	public <T> T getData(String key, Class<T> castTo) {
		return castTo.cast(data.get(key));
	}

	/**
	 * Method to retrieve option by key
	 *
	 * @param key(String) Key to look for
	 *
	 * @return Object stored at the key. Null if no object is stored
	 */
	@Nullable
	public Object getData(String key) {
		return data.get(key);
	}

	public boolean containsData(String key) {
		return data.containsKey(key);
	}
}
