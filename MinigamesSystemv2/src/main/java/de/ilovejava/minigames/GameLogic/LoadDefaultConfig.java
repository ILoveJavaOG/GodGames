package de.ilovejava.minigames.GameLogic;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameSelector.Selector;
import de.ilovejava.minigames.GameSelector.SelectorEvents;
import de.ilovejava.minigames.Games.SnowWar.SnowWar;
import de.ilovejava.minigames.Listeners.*;
import de.ilovejava.minigames.MapTools.MapLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LoadDefaultConfig {

	/**
	 * Function to load everything on boot
	 */
	public static void load() {
		//Create an example yml to explain basics
		File f = new File("plugins/Minigames/ExampleConfig.yml");
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				if (!f.getParentFile().mkdir()) {
					Bukkit.getConsoleSender().sendMessage("§3Could not create folder: 'Minigames' in 'plugins'");
					return;
				}
			}
			YamlConfiguration config = new YamlConfiguration();
			String header =
			"This is the map syntax for all minigames" +
			"\n" +
			"\n" +
			"Every map is on one world!" +
			"World: ExampleWorld" +
			"\n" +
			"(You can enter as many locations as you like)" +
			"LocationName1:" + "\n" +
			"    x: X coordinate of the location" + "\n" +
			"    y: Y coordinate of the location" + "\n" +
			"    z: Z coordinate of the location" + "\n" +
			"You can also enter multiple extra keys to be stored" + "\n" +
			"LocationName2:" + "\n" +
			"    x: X coordinate of the location" + "\n" +
			"    y: Y coordinate of the location" + "\n" +
			"    z: Z coordinate of the location" + "\n" +
			"    Key1: SomeInformation" + "\n" +
			"    Key2: ExtraInformation";
			config.options().header(header);
			try {
				config.save(f);
				Bukkit.getConsoleSender().sendMessage("§2Created ExampleConfig.yml");
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("§3Could not save ExampleConfig.yml");
			}
		}
		Bukkit.getConsoleSender().sendMessage("Registering events");
		//Register events and create selectors
		Bukkit.getServer().getPluginManager().registerEvents(new LeaveListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new SelectorEvents(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new BoatListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new InteractListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new DamageListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new FishListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new ProjectileListener(), Lobby.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new TeleportListener(), Lobby.getPlugin());
		MapLoader.loadMaps();
		//new Selector(IceScooterRace.factory);
		//new Selector(IceScooterPVP.factory);
		//new Selector(IceScooterTimeTrial.factory);
		//new Selector(FishFight.factory);
		new Selector(SnowWar.factory);
	}
}
