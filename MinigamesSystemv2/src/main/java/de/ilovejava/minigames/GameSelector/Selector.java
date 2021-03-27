package de.ilovejava.minigames.GameSelector;

import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.GameLogic.GameOptions;
import de.ilovejava.minigames.GameLogic.GameState;
import de.ilovejava.minigames.MapTools.GameMap;
import de.ilovejava.minigames.MapTools.MapLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class which represents a selector to choose the maps
 */
public class Selector implements InventoryHolder {

	//All selectors
	public static HashMap<String, Selector> selectors = new HashMap<>();

	//Display to choose map in
	private final Inventory selector;

	//List of IDs which are active
	private final List<Integer> loadedGames = new ArrayList<>();

	//Factory to create games
	private final GameFactory<? extends Game> factory;

	//Type of the game the selector contains
	private final String game;

	/**
	 * Constructor to create a selector
	 *
	 * @param factory(GameFactory) Factory which is responsible for game creation
	 */
	public Selector(@NotNull GameFactory<? extends Game> factory) {
		this.game = factory.getGame();
		this.selector = Bukkit.createInventory(this, 9, factory.getTitle());
		this.factory = factory;
		//Create all displays and create games
		fillMaps();
		selectors.put(game, this);
	}

	private void fillMaps() {
		//Get maps for the current game
		for (GameMap map : MapLoader.allMaps.get(game)) {
			loadMap(map);
		}
	}

	private void loadMap(GameMap map) {
        //Create the game and create the item to display each map
		Game game = factory.createGame(map);
		loadedGames.add(game.getId());
		ItemStack display = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = display.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(map.getStringOption(GameOptions.TITLE.name()));
			List<String> lore = new ArrayList<>();
			lore.add("State: Waiting");
			lore.add("Current Players: 0");
			lore.add("Minimum Players: " + map.getIntOption(GameOptions.MINPLAYERS.name()));
			lore.add("Maximum Players: " + map.getIntOption(GameOptions.MAXPLAYERS.name()));
			meta.setLore(lore);
			meta.addItemFlags();
		}
		display.setItemMeta(meta);
		selector.setItem(0, display);
	}
	public void nextGame(int closedGame, GameMap map) {
		loadedGames.remove((Integer) closedGame);
		Game.allGames.remove(closedGame);
		loadMap(map);
	}
	/**
	 * Method to get the hold inventory
	 *
	 * @return Inventory with selectable maps
	 */
	@Override
	public @NotNull Inventory getInventory() {
		return selector;
	}

	/**
	 * Method to update the display of current players on the item
	 *
	 * @param slot(int) Slot at which item needs to be updated
	 */
	private void updatePlayerCount(int slot) {
		ItemStack display = getInventory().getItem(slot);
		assert display != null;
		ItemMeta meta = display.getItemMeta();
		//Change the current player attribute
		if (meta != null) {
			List<String> lore = meta.getLore();
			assert lore != null;
			lore.set(1, "Current Players: "  + Game.allGames.get(loadedGames.get(slot)).getNumPlayers());
			meta.setLore(lore);
		}
		display.setItemMeta(meta);
		getInventory().setItem(slot, display);
	}

	/**
	 * Method which is called when a player selects a game inside the selector
	 * @see SelectorEvents#onClick(InventoryClickEvent)
	 *
	 * @param numMap(int) Slot which got clicked
	 * @param joining(Player) Player which clicked
	 */
	public void join(int numMap, Player joining) {
		//Check if the clicked slot is valid and if the player is not playing
		if (numMap < loadedGames.size() && !Tracker.isInGame(joining)) {
			//Get the id and notify that a player has joined
			int gameId = loadedGames.get(numMap);
			Game game = Game.allGames.get(gameId);
			if (game.getState() == GameState.WAIT) {
				game.joiningPlayer(joining);
				updatePlayerCount(numMap);
			}
		}
	}

	/**
	 * Method to handle player leaving
	 *
	 * @param gameId(int): Id of the changed game
	 */
	public void leaving(int gameId) {
		int leave = loadedGames.indexOf(gameId);
		if (leave != -1) updatePlayerCount(loadedGames.indexOf(gameId));
	}
}
