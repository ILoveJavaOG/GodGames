package de.ilovejava.minigames.GameLogic;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameSelector.Selector;
import de.ilovejava.minigames.MapTools.GameMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Core class to represent a game
 */
public abstract class Game {

	//Map with all games
	public static HashMap<Integer, Game> allGames = new HashMap<>();

	//Counter for the games
	private static int globalId = 0;

	//Id of the game
	@Getter
	private final int id;

	//Players who are actively participating
	protected Set<Player> activePlayers = new HashSet<>();

	//Players who are just watching the game
	protected Set<Player> watchingPlayers = new HashSet<>();

	//Name of the game
	@Getter
	private final String name;

	//Map of the game
	@Getter
	protected GameMap gameMap;

	//State of the game
	@Getter
	protected GameState state = GameState.WAIT;

	//Mark if game is currently loading
	private boolean isLoading = false;

	//Id of the current loading task
	private Integer loadingTask = null;

	private final int defaultLoadingTime = 10;
	//Default load time
	private int loadingTime = defaultLoadingTime;

	protected HashMap<Class<?>, Method> gameEvents = new HashMap<>();

	protected Events eventHandler;

	/**
	 * Constructor to create a game
	 *
	 * @param name(String) Type of the game
	 */
	public Game(String name) {
		this.name = name;
		//Set id and increment counter
		this.id = globalId;
		globalId += 1;
		allGames.put(id, this);
	}

	/**
	 * Utility method to get number of players inside the game
	 *
	 * @return Number of players playing and watching
	 */
	public int getNumPlayers() {
		return activePlayers.size() + watchingPlayers.size();
	}

	/**
	 * Method which is called when a player is joining the game via the selector
	 * @see  de.ilovejava.minigames.GameSelector.Selector
	 *
	 * @param joinedPlayer(Player) Player which is joining
	 */
	public void joiningPlayer(@NotNull Player joinedPlayer) {
		System.out.println("Joining: " + getName() + " on map: " + getGameMap().getMapName());
		//Close selector
		joinedPlayer.closeInventory();
		//Register player in the game
		activePlayers.add(joinedPlayer);
		Tracker.registerPlayer(joinedPlayer, getId());
		this.playerJoin(joinedPlayer);
		//Start loading if minimum number of players is reached
		if (this.getGameMap().getOption(GameOptions.MINPLAYERS.name(), Integer.class) <= getNumPlayers() && !isLoading) {
			load();
		}
	}

	/**
	 * Loading sequence for any game
	 */
	private void load() {
		//Change loading state and start loading
		isLoading = true;
		loadingTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
			activePlayers.forEach(player -> player.setLevel(loadingTime));
			if (loadingTime == 30) {
				sendLoading(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			} else if ((loadingTime % 10 == 0 && loadingTime > 5) || (loadingTime > 0 && loadingTime <= 5)) {
				sendLoading(Sound.ENTITY_PLAYER_LEVELUP);
			}
			loadingTime--;
			if (loadingTime <= 0) {
				stopLoad();
			}
		}, 0, 20L);
	}

	/**
	 * Method which is called when a player leaves during loading and there would not be enough players
	 */
	private void abortLoad() {
		activePlayers.forEach(player -> player.sendMessage("Waiting for players"));
		cancelLoad();
	}

	/**
	 * Utility method to stop and reset the loading sequence
	 */
	private void cancelLoad() {
		Bukkit.getScheduler().cancelTask(loadingTask);
		loadingTask = null;
		isLoading = false;
		loadingTime = defaultLoadingTime;
	}

	/**
	 * Method to stop the loading sequence and start the game
	 */
	private void stopLoad() {
		//Reset loading sequence and change the game state
		cancelLoad();
		state = GameState.INGAME;
		activePlayers.forEach(player -> player.sendMessage("Starting game"));
		this.startGame();
	}

	/**
	 * Utility method to send a message to all players waiting and play a sound
	 *
	 * @param sound(Sound) Sound which is to be played
	 */
	private void sendLoading(Sound sound) {
		activePlayers.forEach(player -> {
			player.sendMessage(ChatColor.GREEN + "Das Spiel restartet in " + ChatColor.YELLOW + loadingTime + ChatColor.GREEN + " Sekunden!");
			player.playSound(player.getLocation(), sound, 1, 1);
		});
	}

	/**
	 * Method which is called when a players leaves or is kicked
	 *
	 * @param leavingPlayer(Player) Player which is leaving
	 */
	public void leavingPlayer(@NotNull Player leavingPlayer) {
		System.out.println("Leaving: " + getName() + " on map: " + getGameMap().getMapName());
		//Remove player from sets
		activePlayers.remove(leavingPlayer);
		watchingPlayers.remove(leavingPlayer);
		Tracker.unregisterPlayer(leavingPlayer);
		this.playerLeave(leavingPlayer);
		//Stop loading if not enough players
		if (this.getGameMap().getOption(GameOptions.MINPLAYERS.name(), Integer.class) > getNumPlayers() && isLoading) {
			abortLoad();
		}
	}

	protected void gameOver() {
		activePlayers.forEach(Tracker::unregisterPlayer);
		watchingPlayers.forEach(Tracker::unregisterPlayer);
		Selector selector = Selector.selectors.get(name);
		selector.nextGame(getId(), gameMap);
	}

	protected void registerEvents(Events eventHandler) {
		this.eventHandler = eventHandler;
		//Look for methods in event
		Method[] foundEvents = this.eventHandler.getClass().getMethods();
		for (Method method : foundEvents) {
			//Check if method is public
			if (Modifier.isPublic(method.getModifiers())) {
				//Check if only param is of type Event
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length == 1) {
					Class<?> param = parameters[0];
					if (Event.class.isAssignableFrom(param)) {
						//Map class to method call
						gameEvents.put(param, method);
					}
				}
			}
		}
	}

	public void callEvent(Event event) {
		//Check if event exists
		Class<?> eventType = event.getClass();
		if (state == GameState.INGAME && gameEvents.containsKey(eventType)) {
			try {
				//Invoke the event
				gameEvents.get(eventType).invoke(eventHandler, event);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Method to set the map for the game
	 *
	 * @param map(GameMap) Map of the game
	 */
	public void loadMap(GameMap map) {
		this.gameMap = map;
	}

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	protected abstract void setup();

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	protected abstract void playerJoin(Player player);

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player(Player) Leaving player
	 */
	protected abstract void playerLeave(Player player);

	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	protected abstract void startGame();

	/**
	 * Method to be called when the game is over
	 */
	protected abstract void stopGame();
}
