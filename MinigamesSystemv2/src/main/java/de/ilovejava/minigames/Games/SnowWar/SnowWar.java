package de.ilovejava.minigames.Games.SnowWar;

import com.mojang.datafixers.util.Pair;
import de.ilovejava.ItemStackBuilder.ItemStackBuilder;
import de.ilovejava.ItemStackBuilder.Triple;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class to represent the fish fight game
 */
public class SnowWar extends Game {

	protected static final ItemStack defaultBall = new ItemStackBuilder(Material.SNOWBALL)
			.getMetaDataBuilder()
			.setDisplayName("Normal Ball")
			.build().build();

	protected static final ItemStack fastBall = new ItemStackBuilder(Material.FIREWORK_STAR)
			.getMetaDataBuilder()
			.setDisplayName("Fast Ball")
			.build().build();

	protected static final ItemStack sonarItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Sonar")
			.setLore("Toete 1 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack supplyDropItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2SupplyDrop")
			.setLore("Toete 2 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack bomberItemPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Bomber")
			.setLore("Toete 3 Spieler um diese Streak freizuschalten")
			.build().build();

	protected static final ItemStack presentPlaceHolder = new ItemStackBuilder(Material.BARRIER)
			.getMetaDataBuilder()
			.setDisplayName("§2Geschenk")
			.setLore("Sammle Geschenke!")
			.build().build();

	static {

		NamespacedKey fastBallKey = new NamespacedKey(Lobby.getPlugin(), "FastBall");
		NamespacedKey defaultBallKey = new NamespacedKey(Lobby.getPlugin(), "DefaultBall");

		ShapelessRecipe recipeFastBall = new ShapelessRecipe(fastBallKey, fastBall);
		ShapelessRecipe recipeDefaultBall = new ShapelessRecipe(defaultBallKey, defaultBall);

		recipeFastBall.addIngredient(Material.SNOWBALL);

		recipeDefaultBall.addIngredient(Material.FIREWORK_STAR);


		Bukkit.getServer().addRecipe(recipeFastBall);
		Bukkit.getServer().addRecipe(recipeDefaultBall);

	}

	//Name of the game
	private static final String name = "SnowWar";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//Set containing the spots for present collection. First is location. Second is a counter and third is for timing
	protected HashMap<Location, Pair<Integer, Long>> presentSpots = new HashMap<>();

	//Set containing the spots for snowball collection. First is location. Second is a counter and third is for timing
	protected HashMap<Location, Pair<Integer, Long>> snowballSpots = new HashMap<>();

	//List of the available streaks sorted by the requirement
	private final List<Pair<KillStreak, Integer>> streakRequirements = new ArrayList<>();

	//List of spawn locations per team
	private List<List<Location>> teamSpawns = new ArrayList<>();

	final HashMap<Player, Triple<Integer, Integer, Integer>> playerData = new HashMap<>();
	//Factory to work with selector
	public static final GameFactory<SnowWar> factory = new GameFactory<SnowWar>() {

		@Override
		public String getGame() {
			return name;
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public String getTitle() {
			return "§2SnowWar";
		}

		@NotNull
		@Override
		public SnowWar createGame(GameMap map) {
			SnowWar newGame = new SnowWar();
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
		for (KillStreak streak : KillStreak.values()) {
			Integer requirement = gameMap.getOption(streak.name(), Integer.class);
			streakRequirements.add(new Pair<>(streak, requirement));
		}
		streakRequirements.sort(Comparator.comparing(Pair::getSecond));
		Integer maxNumberOfTeams = gameMap.getOption("NUMTEAMS", Integer.class);
		System.out.println("NUMBER OF TEAMS: " + maxNumberOfTeams);
		teamSpawns = new ArrayList<>(maxNumberOfTeams);
		for (int i = 0; i < maxNumberOfTeams; i++) {
			teamSpawns.add(new ArrayList<>());
		}
		for (CustomLocation location : gameMap.getLocations()) {
			if (location.containsData("SPAWN")) {
				Integer team = location.getData("TEAM", Integer.class);
				assert team != null;
				teamSpawns.get(team - 1).add(location.getLocation());
			} else if (location.containsData("SNOWBALL")) {
				snowballSpots.put(location.getLocation(), new Pair<>(0, 0L));
			} else if (location.containsData("PRESENT")) {
				presentSpots.put(location.getLocation(), new Pair<>(0, 0L));
			}
		}
	}

	/**
	 * Constructor to create a game
	 */
	public SnowWar() {
		super(name);
		registerEvents(new SnowWarEvents(this));
	}


	/**
	 * Method to be called when the game is starting.
	 * Will be called after loading is done. Game state will be INGAME
	 */
	@Override
	public void startGame() {
		int numTeams = teamSpawns.size();
		Random random = new Random();
		ArrayList<Integer> unfilledTeams = new ArrayList<>(numTeams);
		for (int i = 1; i < numTeams + 1; i++) {
			unfilledTeams.add(i);
		}
		for (Player player : activePlayers) {
			if (unfilledTeams.isEmpty()) {
				for (int i = 1; i < numTeams + 1; i++) {
					unfilledTeams.add(i);
				}
			}
			int team = unfilledTeams.remove(random.nextInt(unfilledTeams.size()));
			playerData.put(player, new Triple<>(0, 0, team));
			List<Location> possibleSpawns = teamSpawns.get(team - 1);
			Location spawn = possibleSpawns.get(random.nextInt(possibleSpawns.size()));
			player.teleport(spawn);
			System.out.println("TEAM: " + team);
			PlayerInventory inventory = player.getInventory();
			inventory.setItem(0, sonarItemPlaceHolder);
			inventory.setItem(1, supplyDropItemPlaceHolder);
			inventory.setItem(2, bomberItemPlaceHolder);
			inventory.setItem(3, presentPlaceHolder);
		}
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		gameOver();
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
	 * Method which is to be called when a player gets killed
	 *
	 * @param playerDowned(Player): Player who got killed
	 */
	public void playerKill(Player playerDowned) {
		System.out.println("DEATH FROM: " + playerDowned);
		Triple<Integer, Integer, Integer> playerStats = playerData.get(playerDowned);
		int team = playerStats.getThird();
		Triple<Integer, Integer, Integer> newStats = new Triple<>(playerStats.getFirst(), 0, team);
		playerData.put(playerDowned, newStats);
		Random random = new Random();
		List<Location> possibleSpawns = teamSpawns.get(team - 1);
		Location location = possibleSpawns.get(random.nextInt(possibleSpawns.size()));
		playerDowned.teleport(location);
	}

	public void increaseKill(Player source) {
		System.out.println("KILL FROM: " + source);
		Triple<Integer, Integer, Integer> playerStats = playerData.get(source);
		Integer currentStreak = playerStats.getSecond() + 1;
		playerStats.setFirst(playerStats.getFirst() + 1);
		playerStats.setSecond(currentStreak);
		playerData.put(source, playerStats);
		for (int i = 0; i < streakRequirements.size(); i++) {
			Pair<KillStreak, Integer> streak = streakRequirements.get(i);
			if (streak.getSecond().equals(currentStreak)) {
				System.out.println("ACHIEVED STREAK");
				//Set item
				source.getInventory().setItem(i, streak.getFirst().getDisplay());
				break;
			}
		}
	}

	public void activateSonar(Player player) {
		int team = playerData.get(player).getThird();
		activePlayers.stream().filter(other -> playerData.get(other).getThird() != team)
				.forEach(other -> other.setGlowing(true));
		player.getInventory().setItem(0, sonarItemPlaceHolder);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(),
				() -> activePlayers.forEach(playing -> playing.setGlowing(false)), 8*20L);
	}

	public void activateSupplyDrop(Player player, Location dropSpot) {
		System.out.println("REQUESTING SUPPLY DROP");
		player.getInventory().setItem(1, supplyDropItemPlaceHolder);
		Random randomGen = new Random();
		//Make rocket spawn above the ground
		Firework rocket = player.getWorld().spawn(dropSpot, Firework.class);
		//Build firework effects
		FireworkMeta meta = rocket.getFireworkMeta();
		FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.withColor(Color.fromBGR(randomGen.nextInt(256), randomGen.nextInt(256), randomGen.nextInt(256)));
		builder.withTrail();
		builder.withFade(Color.fromBGR(randomGen.nextInt(256), randomGen.nextInt(256), randomGen.nextInt(256)));
		meta.addEffect(builder.build());
		meta.setPower(3);
		rocket.setFireworkMeta(meta);
		rocket.setMetadata("UUID", new FixedMetadataValue(Lobby.getPlugin(), player.getUniqueId().toString()));
	}

	public void activateBomber(Player player) {
		player.getInventory().setItem(2, bomberItemPlaceHolder);
		Projectile thrown = player.launchProjectile(EnderPearl.class);
		thrown.setMetadata("UUID", new FixedMetadataValue(Lobby.getPlugin(), player.getUniqueId().toString()));
	}
	public void enableRapidFire(Player holder) {
		SnowWarEvents snowWarEvents = (SnowWarEvents) eventHandler;
		snowWarEvents.enableRapidFire(holder);
	}
}
