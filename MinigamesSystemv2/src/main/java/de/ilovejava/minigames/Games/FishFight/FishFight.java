package de.ilovejava.minigames.Games.FishFight;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.GameSelector.Selector;
import de.ilovejava.minigames.Games.FishFight.Items.Rod;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class to represent the fish fight game
 */
public class FishFight extends Game {

	//Name of the game
	private static final String name = "FishFight";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Factory to work with selector
	public static final GameFactory<FishFight> factory = new GameFactory<FishFight>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2FishFight";
		}

		@NotNull
		@Override
		public FishFight createGame(GameMap map) {
			FishFight newGame = new FishFight();
			newGame.loadMap(map);
			newGame.setup();
			return newGame;
		}
	};

	/**
	 * Method to setup the game. Must be called by game itself
	 */
	@Override
	protected void setup() {
		spawns = gameMap.getLocations().stream()
				.filter((CustomLocation loc) -> loc.getData("SPAWN") != null)
				.collect(Collectors.toList());
	}

	//Events will be handled by this class
	private final FishFightEvents events = new FishFightEvents();

	//List of possible spawns
	private List<CustomLocation> spawns = new ArrayList<>();

	/**
	 * Constructor to create a game
	 */
	public FishFight() {
		super(name);
	}


	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		Rod rod = new Rod();
		//Add for faster natural fishing speed
		rod.addEnchantment(Enchantment.LUCK, 3);
		//Place players into random boats
		Random generator = new Random();
		for (Player playing : activePlayers) {
			Bukkit.getOnlinePlayers().forEach((Player hiden) -> playing.showPlayer(Lobby.getPlugin(), hiden));
			playing.getInventory().setItem(0, rod.getRod());
			//Reset player stats
			playing.setHealth(20.f);
			playing.setFoodLevel(20);
			playing.setLevel(0);
			playing.setExp(0.0f);
			//Get random spawn point and remove it
			int random = generator.nextInt(activePlayers.size());
			CustomLocation spawnPoint = spawns.remove(random);
			playing.teleport(spawnPoint.getLocation());
		}
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		Selector selector = Selector.selectors.get(name);
		selector.nextGame(getId(), gameMap);
	}

	/**
	 * Method to be executed when a player joins the game
	 * Active players will be updated beforehand
	 *
	 * @param player(Player) Joining player
	 */
	@Override
	public void playerJoin(Player player) {
		//Nothing needs to be done right now
	}

	/**
	 * Method to be executed when a player leaves the game
	 * Active players and watching players will be updated beforehand
	 *
	 * @param player(Player) Leaving player
	 */
	@Override
	public void playerLeave(Player player) {
		//Stop the game if there arent any more players
		if (activePlayers.size() <= 1) {
			stopGame();
		}
	}

	/**
	 * Event listener
	 *
	 * @param event(Event) Event which is called and regards the game
	 */
	@Override
	public void callEvent(Event event) {
		if (event instanceof PlayerFishEvent) {
			events.onFish((PlayerFishEvent) event);
		} else if (event instanceof PlayerDropItemEvent) {
			events.onDrop((PlayerDropItemEvent) event);
		} else if (event instanceof EntityDamageByEntityEvent) {
			events.onDamage((EntityDamageByEntityEvent) event);
		} else if (event instanceof EntityRegainHealthEvent) {
			events.onRegen((EntityRegainHealthEvent) event);
		} else if (event instanceof FoodLevelChangeEvent) {
			events.onFoodDepletion((FoodLevelChangeEvent) event);
		} else if (event instanceof PlayerItemConsumeEvent) {
			events.onConsumption((PlayerItemConsumeEvent) event);
		}
	}

	/**
	 * Method which is to be called when a player gets killed
	 *
	 * @param receiver(Player): Player who got killed
	 */
	public void playerKill(Player receiver) {
		activePlayers.remove(receiver);
		watchingPlayers.add(receiver);
		receiver.teleport(spawns.get(0).getLocation());
		if (activePlayers.size() == 1) {
			stopGame();
		}
	}
}
