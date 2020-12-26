package de.ilovejava.minigames.MapTools;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to load and parse maps
 */
public class MapLoader {

	public static HashMap<String, List<GameMap>> allMaps = new HashMap<>();

	public static void loadMaps() {
		//Check if maps can be accessed
		File gameFolder = new File("plugins/Minigames/");
		if (!gameFolder.exists()) {
			if (!gameFolder.mkdir()) {
				Bukkit.getConsoleSender().sendMessage("§3Could not create: " + gameFolder.getPath());
			}
		}
		for (File game : gameFolder.listFiles()) {
			if (game.isDirectory()) {
				Bukkit.getConsoleSender().sendMessage("§4Loadings maps for game: " + game);
				File[] maps = game.listFiles();
				int validMaps = 0;
				List<GameMap> all = new ArrayList<>();
				for (File map : maps) {
					if (map.isFile()) {
						try {
							all.add(parseMap(map));
							validMaps++;
						} catch (ParserException e) {
							Bukkit.getConsoleSender().sendMessage("§3Could not load map: " + map.getName());
							e.printStackTrace();
						}
					}
				}
				allMaps.put(game.getName(), all);
				Bukkit.getConsoleSender().sendMessage("§4" + validMaps + " found");
			}
		}
	}

	/**
	 * Method to parse a map from file
	 *
	 * @param mapFile(File) File to parse
	 *
	 * @throws ParserException If the parser detects invalid syntax
	 */
	private static GameMap parseMap(File mapFile) throws ParserException {
		//Check if the file can be loaded as YAML config
		YamlConfiguration mapConfig = new YamlConfiguration();
		World loadedWorld;
		try {
			mapConfig.load(mapFile);
		} catch (InvalidConfigurationException e) {
			ParserException exception = new ParserException("Invalid YAML syntax for map: " + mapFile.getPath());
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		} catch (IOException e) {
			ParserException exception = new ParserException("Something went wrong while trying to read the map file");
			exception.setStackTrace(e.getStackTrace());
			throw exception;
		}
		//Check if parsed map exists and is valid
		String world = mapConfig.getString("World");
		if (world == null) {
			throw new ParserException("§3No specified world in: " + mapFile.getPath());
		} else {
			loadedWorld = Bukkit.getWorld(world);
			if (loadedWorld == null) {
				throw new ParserException("§3Could not find world: " + world + " which is specified in " + mapFile.getPath());
			}
		}
		//Load locations
		ConfigurationSection locations = mapConfig.getConfigurationSection("Locations");
		List<CustomLocation> allLocations = new ArrayList<>();
		//Check if section for locations exists
		if (locations == null) {
			throw new ParserException("§3No locations are specified in: " + mapFile.getPath());
		} else {
			//Iterate over the locations
			Map<String, Object> locationMap = locations.getValues(false);
			for (Map.Entry<String, Object> location : locationMap.entrySet()) {
				//Get the location name
				String locationName = location.getKey();
				MemorySection section = (MemorySection) location.getValue();
				//Check for x, y and z coordinates
				for (String coordinates : new String[]{"x", "y", "z"}) {
					if (!section.contains(coordinates)) {
						throw new ParserException("Could not retrieve " + coordinates + " coordinate from " + locationName + " in " + mapFile.getPath());
					}
				}
				double x = section.getDouble("x");
				double y = section.getDouble("y");
				double z = section.getDouble("z");
				//Remove x,y and z so remaining data is optional data
				Map<String, Object> extraData = section.getValues(false);
				extraData.remove("x");
				extraData.remove("y");
				extraData.remove("z");
				//Create location and add to list
				Location buildLocation = new Location(loadedWorld, x, y, z);
				allLocations.add(new CustomLocation(locationName, buildLocation, extraData));
			}
		}
		//Parse items
		ConfigurationSection itemSection = mapConfig.getConfigurationSection("Items");
		List<String> itemNames = new ArrayList<>();
		List<Pair<Double, Double>> itemProbabilities = new ArrayList<>();
		if (itemSection != null) {
			//Iterate over the locations
			Map<String, Object> itemMap = itemSection.getValues(false);
			for (Map.Entry<String, Object> itemEntry : itemMap.entrySet()) {
				String itemName = itemEntry.getKey();
				ConfigurationSection stats = (ConfigurationSection) itemEntry.getValue();
				//Check for x, y and z coordinates
				double min;
				double max;
				if (stats.contains("Min")) {
					min = stats.getDouble("Min");
				} else {
					throw new ParserException("Could not retrieve min probability for item: " + itemName + " in " + mapFile.getPath());
				}
				if (stats.contains("Max")) {
					max = stats.getDouble("Max");
				} else {
					throw new ParserException("Could not retrieve max probability for item: " + itemName + " in " + mapFile.getPath());
				}
				itemNames.add(itemName);
				itemProbabilities.add(new Pair<>(min, max));
			}
		}
		Map<String, Object> options = new HashMap<>();
		ConfigurationSection extraOptions = mapConfig.getConfigurationSection("Options");
		if (extraOptions != null) {
			//Iterate over keys and check for default values
			options = extraOptions.getValues(false);
		}
		//Create the map
		return new GameMap(mapFile.getName().replace(".yml", ""), loadedWorld, options, allLocations, new ProbabilityMap(itemNames, itemProbabilities));
	}
}
