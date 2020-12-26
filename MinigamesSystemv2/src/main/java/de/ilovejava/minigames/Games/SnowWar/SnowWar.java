package de.ilovejava.minigames.Games.SnowWar;

import com.google.common.collect.Sets;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Game;
import de.ilovejava.minigames.GameLogic.GameCommand;
import de.ilovejava.minigames.GameLogic.GameFactory;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.KillStreakCost;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.PlayerStats;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.Presents;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.SnowManStatus;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.*;

/**
 * Class to represent the SnowWar game
 */
public class SnowWar extends Game {

	//Name of the game
	private static final String name = "SnowWar";

	//Register command
	private static final GameCommand command = new GameCommand(name);

	//The currently active internal game state
	protected InternalGameState gameState = InternalGameState.INITIAL;

	//Set containing the spots of placed presents
	protected Set<Location> takenPresentSpots = Sets.newConcurrentHashSet();

	//List of not taken present spots
	protected List<Location> freePresentSpots = new ArrayList<>();

	//Set containing the spots for snowball collection
	protected ConcurrentHashMap<Location, SnowManStatus> takenSnowballSpots = new ConcurrentHashMap<>();

	//List of not taken snowball spots
	protected List<Location> freeSnowBallSpots = new ArrayList<>();

	//List of the available streaks sorted by the requirement
	private final KillStreakCost[] streakRequirements = new KillStreakCost[3];

	//List containing the locations to build the barrier wall
	private final List<Location> wallPoints = new ArrayList<>();

	//Task for snowman spawning
	private int snowManSpawnTask;

	//Task for present spawning
	private int presentTask;

	//List of spawn locations per team
	private List<List<Location>> teamSpawns = new ArrayList<>();

	//Hashmap containing the stats for the player
	final ConcurrentHashMap<Player, PlayerStats> playerData = new ConcurrentHashMap<>();

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
		//Builds the requirements for each kill streak
		KillStreak[] streaks = KillStreak.values();
		for (int i = 0; i < streaks.length; i++) {
			Integer requirement = gameMap.getOption(streaks[i].name(), Integer.class);
			streakRequirements[i] = new KillStreakCost(streaks[i], requirement);
		}
		//Builds arrays to store possible spawn locations
		Integer maxNumberOfTeams = gameMap.getOption("NUMTEAMS", Integer.class);
		teamSpawns = new ArrayList<>(maxNumberOfTeams);
		for (int i = 0; i < maxNumberOfTeams; i++) {
			teamSpawns.add(new ArrayList<>());
		}
		//Parses the custom locations
		for (CustomLocation location : gameMap.getLocations()) {
			if (location.containsData("SPAWN")) {
				Integer team = location.getData("TEAM", Integer.class);
				assert team != null;
				teamSpawns.get(team - 1).add(location.getLocation());
			} else if (location.containsData("SNOWBALL")) {
				freeSnowBallSpots.add(location.getLocation());
			} else if (location.containsData("PRESENT")) {
				freePresentSpots.add(location.getLocation());
			} else if (location.containsData("WALL")) {
				wallPoints.add(location.getLocation());
			}
		}
		//Fill wall
		toggleWall(true);
	}

	/**
	 * Generic method to fill free spots
	 *
	 * @param taken(int): Number of currently taken spots
	 * @param max(int): Upper bound to fill spots
	 * @param freeSpots(List[Location]): List containing available spots
	 * @param action(Consumer[Location]): Consumer to use with free spot
	 */
	protected void fill(int taken, int max, List<Location> freeSpots, Consumer<Location> action) {
		//Check that there are free spots
		if (taken < max) {
			int free = freeSpots.size();
			Random random = new Random();
			//Fill possible spots
			for (int i = 0; i < max - taken; i++) {
				int bound = free - i;
				if (bound > 0) {
					//Get free spot and apply action
					int index = random.nextInt(free - i);
					action.accept(freeSpots.remove(index));
				}
			}
		}
	}

	/**
	 * Utility function to build the barrier wall separating the teams
	 *
	 * @param toggle(boolean): True for building, false for removing
	 */
	private void toggleWall(boolean toggle) {
		//Get needed types
		Material type = toggle ? Material.BARRIER : Material.AIR;
		Material antiType = !toggle ? Material.BARRIER : Material.AIR;
		//Build wall by iterating through points
 		World world = gameMap.getWorld();
		Location first = wallPoints.get(0).clone();
		for (int i = 0; i < wallPoints.size() - 1; i++) {
			Location second = wallPoints.get(i + 1);
			//Get direction from first to second point
			double distance = first.distance(second);
			Vector direction = second.toVector().clone().subtract(first.toVector()).normalize();
			//0.5 is the step size
			int steps = (int) (distance / 0.5);
			for (int step = 0; step < steps; step++) {
				//Fill from top to bottom
				for (int height = 0; height < 256 - first.getY(); height++) {
					Location pillar = first.clone().add(0, height, 0);
					Block block = world.getBlockAt(pillar);
					//Replace types
					if (block.getType() == antiType) block.setType(type, true);
				}
				//Move forward
				first.add(direction.clone().multiply(0.5));
			}
			//Go to next pair of points
			first = second.clone();
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
		//Create array with not yet filled teams
		int numTeams = teamSpawns.size();
		Random random = new Random();
		ArrayList<Integer> unfilledTeams = new ArrayList<>(numTeams);
		for (int i = 1; i <= numTeams; i++) {
			unfilledTeams.add(i);
		}
		//Players will be assigned into a team
		for (Player player : activePlayers) {
			//Refill teams for next batch
			if (unfilledTeams.isEmpty()) {
				for (int i = 1; i <= numTeams; i++) {
					unfilledTeams.add(i);
				}
			}
			//Random team for the player
			int team = unfilledTeams.remove(random.nextInt(unfilledTeams.size()));
			playerData.put(player, new PlayerStats(team));
			//Get possible spawn points for the team and move player to this point
			List<Location> possibleSpawns = teamSpawns.get(team - 1);
			Location spawn = possibleSpawns.get(random.nextInt(possibleSpawns.size()));
			player.teleport(spawn);
			//Set items kill streaks and item placeholders
			PlayerInventory inventory = player.getInventory();
			inventory.setItem(0, sonarItemPlaceHolder);
			inventory.setItem(1, supplyDropItemPlaceHolder);
			inventory.setItem(2, bomberItemPlaceHolder);
			inventory.setItem(3, presentPlaceHolder);
			//Give player gloves for easy snowball pickup
			inventory.setItem(4, gloves);
			//Fill armor slots
			inventory.setArmorContents(SnowWarItemStacks.getArmor());
			//Default hit points
			player.setLevel(5);
		}
		//Fill snowman spots and start building phase
		snowManSpawnTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> fill(takenSnowballSpots.size(), 5, freeSnowBallSpots, this::spawnSnowMan), 0L, 20L*30L);
		gameState = InternalGameState.BUILDING;
		//Initialize fighting phase
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
			//Remove wall
			toggleWall(false);
			gameState = InternalGameState.FIGHTING;
			//Place presents
			presentTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> fill(takenPresentSpots.size(), 5, freePresentSpots, this::spawnPresent), 0L, 20L*30L);
		}, 20L*10);
		//Max time for the game
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), this::stopGame, 20L*10000);
	}

	/**
	 * Method to be called when the game is over
	 */
	@Override
	public void stopGame() {
		//Stop active tasks
		Bukkit.getScheduler().cancelTask(snowManSpawnTask);
		Bukkit.getScheduler().cancelTask(presentTask);
		//Remove placed blocks
		SnowWarEvents events = (SnowWarEvents) eventHandler;
		events.buildBlocks.forEach((location -> location.getBlock().setType(Material.AIR, true)));
		events.oldSessions.forEach(session -> session.undo(session));
		takenPresentSpots.forEach(taken -> taken.getBlock().setType(Material.AIR, true));
		//Remove particle trails
		for (Projectile projectile : events.particleTrails.keySet()) {
			events.particleTrails.remove(projectile);
			projectile.remove();
		}
		//Remove snowmen
		for (Map.Entry<Location, SnowManStatus> entry : takenSnowballSpots.entrySet()) {
			takenSnowballSpots.remove(entry.getKey());
			entry.getValue().getSnowman().remove();
		}
		//Call default game over method
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
		player.getInventory().clear();
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
	 * Method which is to be called when a player gets hit by a snowball
	 *
	 * @param hitPlayer(Player): Player who got hit
	 */
	public void playerHit(Player hitPlayer) {
		//Check if a player will die by this
		if (hitPlayer.getLevel() == 1) {
			playerDeath(hitPlayer);
		//Decrease level ("Hit count")
		} else {
			hitPlayer.setLevel(hitPlayer.getLevel() - 1);
		}
	}

	/**
	 * Method which is to be called when a player is being killed according to the game
	 *
	 * @param deadPlayer(Player): Player who died
	 */
	private void playerDeath(Player deadPlayer) {
		System.out.println("DEATH");
		Location deathSpot = deadPlayer.getLocation();
		//Reset streak
		playerData.computeIfPresent(deadPlayer, (player, stats) -> new PlayerStats(stats.getHits(), 0, stats.getTeam()));
		Random random = new Random();
		//Spawn player
		List<Location> possibleSpawns = teamSpawns.get(playerData.get(deadPlayer).getTeam() - 1);
		Location location = possibleSpawns.get(random.nextInt(possibleSpawns.size()));
		deadPlayer.teleport(location);
		//Reset hit and streak
		deadPlayer.setLevel(5);
		deadPlayer.setExp(0.0f);
		//Drop loot at death spot
		dropLoot(deathSpot);
	}

	/**
	 * Method to drop loot onto the ground if a player dies
	 *
	 * @param deathSpot(Location): Location of where to drop the loot
	 */
	private void dropLoot(Location deathSpot) {
		Random random = new Random();
		//Number of items to drop
		int numDropsSnowballs = random.nextInt(10);
		int numDropsSnow = random.nextInt(5);
		World world = gameMap.getWorld();
		//Drop items onto the ground
		for (int i = 0; i < numDropsSnowballs; i++) {
			Location offset = deathSpot.clone().add((random.nextInt(3) - 1)*0.5, 0, (random.nextInt(3) - 1)*0.5);
			world.dropItem(offset, fastBall);
		}
		for (int i = 0; i < numDropsSnow; i++) {
			Location offset = deathSpot.clone().add((random.nextInt(3) - 1)*0.5, 0, (random.nextInt(3) - 1)*0.5);
			world.dropItem(offset, new ItemStack(Material.SNOW_BLOCK));
		}
	}

	/**
	 * Method to be called when a player gets a kill
	 *
	 * @param source(Player): Player who got killed
	 */
	public void increaseHit(Player source) {
		//Increase kill streak
		int currentStreak = playerData.get(source).getStreak() + 1;
		playerData.computeIfPresent(source, (player, stats) -> new PlayerStats(stats.getHits() + 1, currentStreak, stats.getTeam()));
		//Check which requirement is active
		for (int i = 0; i < 3; i++) {
			KillStreakCost streak = streakRequirements[i];
			int streakCost = streak.getCost();
			if (streakCost == currentStreak) {
				//Set item
				source.getInventory().setItem(i, streak.getStreak().getDisplay());
				break;
			}
		}
		//Max streak already reached
		if (streakRequirements[2].getCost() <= currentStreak){
			source.setExp(1.0f);
		//Calculate the streak progress
		} else {
			setStreakProgress(source);
		}
	}

	/**
	 * Method to calculate the streak progress for the player
	 *
	 * @param source(Player): Player to get progress for
	 */
	private void setStreakProgress(Player source) {
		int currentStreak = playerData.get(source).getStreak();
		for (int i = 0; i < 3; i++) {
			//Get previous and next cost
			float previousCost = (i == 0 ? 0 : streakRequirements[i - 1].getCost());
			float nextCost = streakRequirements[i].getCost();
			//Check if streak is inside bound
			if (previousCost <= currentStreak && currentStreak < streakRequirements[i].getCost()) {
				currentStreak -= previousCost;
				nextCost -= previousCost;
				//Calculate percentage
				source.setExp(1.0f - (nextCost - currentStreak)/nextCost);
				break;
			}
		}
	}

	/**
	 * Method to be called when a player is activating his sonar
	 *
	 * @param player(Player): Player who activated the streak
	 */
	protected void activateSonar(Player player) {
		//Remove streak
		player.getInventory().setItem(0, sonarItemPlaceHolder);
		//Get the team of the person activating the streak
		int team = playerData.get(player).getTeam();
		//Make other teams glowing and not glowing
		activePlayers.stream().filter(other -> playerData.get(other).getTeam() != team)
				.forEach(other -> other.setGlowing(true));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(),
				() -> activePlayers.forEach(playing -> playing.setGlowing(false)), 8*20L);
	}

	/**
	 * Method to be called when a player is activating a supply drop
	 *
	 * @param player(Player): Player who activated the streak
	 * @param dropSpot(Location): Location of where to activate the streak
	 */
	protected void activateSupplyDrop(Player player, Location dropSpot) {
		//Remove streak
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
		//Bind item so it can be tracked
		Tracker.bindEntity(rocket, player);
	}

	/**
	 * Method to be called when a player is activating a bomber
	 *
	 * @param bomber(Player): Player who activated the streak
	 */
	protected void activateBomber(Player bomber) {
		//Remove item
		bomber.getInventory().setItem(2, bomberItemPlaceHolder);
		//Throw EnderPearl
		bomber.launchProjectile(EnderPearl.class);
		//Reset streak
		playerData.computeIfPresent(bomber, (player, stats) -> new PlayerStats(stats.getHits(), 0, stats.getTeam()));
		bomber.setExp(0.0f);
	}

	/**
	 * Method to delegate call to the event handler
	 *
	 * @param holder(Player): Player who enabled rapid fire
	 */
	public void enableRapidFire(Player holder) {
		((SnowWarEvents) eventHandler).enableRapidFire(holder);
	}

	/**
	 * Utility function to spawn a snowman at the given spot
	 *
	 * @param snowBallSpot(Location): Location to spawn the snowman at
	 */
	private void spawnSnowMan(Location snowBallSpot) {
		World world = snowBallSpot.getWorld();
		assert world != null;
		//Spawn the snowman and setup
		Snowman snowman = world.spawn(snowBallSpot, Snowman.class);
		snowman.setAI(false);
		snowman.setDerp(true);
		snowman.setInvulnerable(true);
		//New snowman
		takenSnowballSpots.put(snowBallSpot, new SnowManStatus(snowman));
	}

	/**
	 * Utility method to spawn a present
	 *
	 * @param presentSpot(Location): Location to spawn the present at
	 */
	private void spawnPresent(Location presentSpot) {
		//Spawn the present
		Block present = presentSpot.getBlock();
		SkullCreator.blockWithBase64(present, Presents.randomPresent());
		Skull thisSkull = (Skull) present.getState();
		Rotatable skullRotation = (Rotatable) thisSkull.getBlockData();
		skullRotation.setRotation(Presents.randomRotation());
		thisSkull.setBlockData(skullRotation);
		thisSkull.update(true);
		//New present
		takenPresentSpots.add(presentSpot);
	}

	/**
	 * Method to be called when a player gets killed by a shovel
	 *
	 * @param deadPlayer(Player): The player who got shoveled
	 */
	protected void instantKill(Player deadPlayer) {
		Location deathSpot = deadPlayer.getLocation();
		//Player death and double loot
		playerDeath(deadPlayer);
		dropLoot(deathSpot);
	}
}
