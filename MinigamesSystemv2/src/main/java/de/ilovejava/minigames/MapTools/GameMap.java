package de.ilovejava.minigames.MapTools;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent a map for a given game
 */
public class GameMap implements Optionable {

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

	public GameMap(GameMap map) {
		this(map.mapName, map.world, map.options, map.locations, map.probabilities);
	}

	/**
	 * Method to retrieve option from the map
	 *
	 * @param key(DefaultOptions) Key to look at
	 *
	 * @return Object at key. May be null
	 */
	@Override
	@Nullable
	public Object getOption(String key) {
		return options.get(key);
	}

	@Override
	@Nullable
	public <E extends Enum<E> & Options> Object getOption(E key) {
		return options.get(key.name());
	}
 }
