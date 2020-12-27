package de.ilovejava.minigames.MapTools;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent a map for a given game
 */
public class GameMap {

	//World the map is set in
	@Getter
	private final World world;

	//Name of the map
	@Getter
	private final String mapName;

	//Locations contained in the map
	@Getter
	private final List<CustomLocation> locations;

	//Optional options for the map
	@Getter
	private final Map<String, Object> options;

	@Getter
	private final HashMap<String, Pair<Double, Double>> items = new HashMap<>();

	@Getter
	private final ProbabilityMap probabilities;

	/**
	 * Constructor for map
	 *  @param mapName(String) Name of the map
	 * @param world(World) World of the map
	 * @param locations(List) List of locations in the map
	 * @param options(Map): Map containing all options for the map
	 */
	public GameMap(String mapName, World world, Map<String, Object> options, List<CustomLocation> locations, ProbabilityMap items) {
		this.mapName = mapName;
		this.locations = locations;
		this.world = world;
		this.options = options;
		this.probabilities = items;
	}

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 * @param castTo(T) CLass to cast to
	 *
	 * @return Object at key casted to T. May be null
	 */
	@Nullable
	public <T> T getOption(String key, @NotNull Class<T> castTo) {
		return castTo.cast(options.get(key));
	}

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 *
	 * @return Object at key. May be null
	 */
	@Nullable
	public Object getOption(String key) {
		return options.get(key);
	}

	public <E extends Enum<E> & Options> Object getOption(E key) {
		return options.get(key.name());
	}

	public <T, E extends Enum<E> & Options> T getOption(E key, Class<T> castTo) {
		return castTo.cast(options.getOrDefault(key.name(), key.getDefaultValue()));
	}

	public Integer getIntOption(String key) {
		return (Integer) options.get(key);
	}

	public <E extends Enum<E> & Options> Integer getIntOption(E key) {
		return (Integer) options.getOrDefault(key.name(), key.getDefaultValue());
	}

	public Double getDoubleOption(String key) {
		return (Double) options.get(key);
	}

	public <E extends Enum<E> & Options> Double getDoubleOption(E key) {
		return (Double) options.getOrDefault(key.name(), key.getDefaultValue());
	}

	public String getStringOption(String key) {
		return (String) options.get(key);
	}

	public <E extends Enum<E> & Options> String getStringOption(E key) {
		return (String) options.getOrDefault(key.name(), key.getDefaultValue());
	}
 }
