package de.ilovejava.minigames.Games.SnowWar;

import com.google.common.collect.Sets;
import de.ilovejava.minigames.Communication.RunnableWrapper;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.PlayerStats;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.Presents;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.SnowManStatus;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.SpawnDistance;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.*;

public class SnowWarMap extends GameMap {

	//Set containing the spots of placed presents
	protected Set<Location> takenPresentSpots = Sets.newConcurrentHashSet();

	//List of not taken present spots
	protected List<Location> freePresentSpots = new ArrayList<>();

	//Task for present spawning
	private BukkitTask presentTask;

	//List of currently taken snowball spots per team
	protected List<ConcurrentHashMap<Location, SnowManStatus>> takenSnowballSpots = new ArrayList<>();

	//List of not taken snowball spots per team
	protected List<List<Location>> freeSnowBallSpots = new ArrayList<>();

	//Task for snowman spawning
	private BukkitTask snowManSpawnTask;

	//List of spawn locations per team
	private List<List<Location>> teamSpawns;


	//List containing the locations to build the barrier wall
	private final List<Location> wallPoints = new ArrayList<>();


	/**
	 * Constructor for map
	 */
	public SnowWarMap(GameMap map) {
		super(map);
		setup();
	}

	public void toggleWall(boolean toggle) {
		//Get needed types
		Material type = toggle ? Material.BARRIER : Material.AIR;
		Material antiType = !toggle ? Material.BARRIER : Material.AIR;
		//Build wall by iterating through points
		World world = getWorld();
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


	public void setup() {
		int numTeams = getIntOption(GameOptions.NUM_TEAMS);
		teamSpawns = new ArrayList<>(numTeams);
		for (int i = 0; i < numTeams; i++) {
			teamSpawns.add(new ArrayList<>());
			takenSnowballSpots.add(new ConcurrentHashMap<>());
			freeSnowBallSpots.add(new ArrayList<>());
		}
		//Parses the custom locations
		for (CustomLocation location : getLocations()) {
			if (location.containsData("SPAWN")) {
				Integer team = location.getIntOption("TEAM");
				assert team != null;
				teamSpawns.get(team - 1).add(location.getLocation());
			} else if (location.containsData("SNOWBALL")) {
				Integer team = location.getIntOption("TEAM");
				assert team != null;
				freeSnowBallSpots.get(team - 1).add(location.getLocation());
			} else if (location.containsData("PRESENT")) {
				freePresentSpots.add(location.getLocation());
			} else if (location.containsData("WALL")) {
				wallPoints.add(location.getLocation());
			}
		}
	}

	public void spawnPlayers(Set<Player> activePlayers, ConcurrentHashMap<Player, PlayerStats> playerData) {
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
			respawn(player, playerData, activePlayers);
		}
	}

	protected void respawn(Player player, ConcurrentHashMap<Player, PlayerStats> playerData, Set<Player> activePlayers) {
		int team = playerData.get(player).getTeam() - 1;
		//Get possible spawn points for the team and move player to this point
		List<Location> possibleSpawns = teamSpawns.get(team);
		Location spawn = getBestSpawn(possibleSpawns, team, playerData, activePlayers);
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
		inventory.setArmorContents(SnowWarItemStacks.getArmor(team));
		//Default hit points
		player.setLevel(getIntOption(GameOptions.HITPOINTS));
		player.setExp(0);
		player.setFoodLevel(19);
		player.setHealth(20.0);
	}

	/**
	 * Utility method which will find the best possible spawn.
	 *
	 * @param possible(List[Location]): Possible spawns for player
	 * @param team(int): Team of the player
	 * @param activePlayers(Set[Player]): Possible players
	 *
	 * @return Spawn with longest shortest distance to any enemy
	 */
	private Location getBestSpawn(List<Location> possible, int team, ConcurrentHashMap<Player, PlayerStats> playerData, Set<Player> activePlayers) {
		PriorityQueue<SpawnDistance> distances = new PriorityQueue<>();
		for (Player enemy : activePlayers) {
			if (playerData.get(enemy).getTeam() != team) {
				for (Location spawn : possible) {
					double distance = -spawn.distanceSquared(enemy.getLocation());
					distances.add(new SpawnDistance(spawn, distance));
				}
			}
		}
		assert distances.peek() != null;
		return distances.peek().getSpawn();
	}

	public void initSnowman() {
		int numTeams = teamSpawns.size();
		BiConsumer<Location, Integer> snowmanSpawn = this::spawnSnowMan;
		//Fill snowman spots and start building phase
		snowManSpawnTask = new RunnableWrapper(() -> {
			//Spawn per team
			for (int team = 1; team <= numTeams; team++) {
				List<Location> spots = freeSnowBallSpots.get(team - 1);
				int finalTeam = team;
				fill(takenSnowballSpots.get(team - 1).size(),
						getIntOption(GameOptions.MAX_NUM_SNOWMAN),
						spots,
						(location -> snowmanSpawn.accept(location, finalTeam)));
			}
		}).runTaskTimerAsynchronously(0L, 20L*getIntOption(GameOptions.DELAY_SNOWMAN));
	}

	public void initPresents() {
		//Place presents
		presentTask = new RunnableWrapper(
				() -> fill(takenPresentSpots.size(), getIntOption(GameOptions.MAX_NUM_PRESENT), freePresentSpots, this::spawnPresent))
		.runTaskTimerAsynchronously(0L, 20L*getIntOption(GameOptions.DELAY_PRESENT));
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
	 * Utility function to spawn a snowman at the given spot
	 *
	 * @param snowBallSpot(Location): Location to spawn the snowman at
	 */
	private void spawnSnowMan(Location snowBallSpot, int team) {
		World world = getWorld();
		world.playSound(snowBallSpot, Sound.BLOCK_BEEHIVE_EXIT, 1.0f, 1.0f);
		//Spawn the snowman and setup
		Snowman snowman = world.spawn(snowBallSpot, Snowman.class);
		snowman.setAI(false);
		snowman.setDerp(true);
		snowman.setInvulnerable(true);
		//New snowman
		takenSnowballSpots.get(team - 1).put(snowBallSpot, new SnowManStatus(snowman));
	}

	/**
	 * Utility method to spawn a present
	 *
	 * @param presentSpot(Location): Location to spawn the present at
	 */
	private void spawnPresent(Location presentSpot) {
		getWorld().playSound(presentSpot, Sound.ENTITY_EVOKER_CAST_SPELL, 1.0f, 1.0f);
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
	 * Method to drop loot onto the ground if a player dies
	 *
	 * @param deathSpot(Location): Location of where to drop the loot
	 */
	protected void dropLoot(Location deathSpot) {
		Random random = new Random();
		//Number of items to drop
		int numDropsSnowballs = random.nextInt(getIntOption(GameOptions.NUM_DROP_SNOWBALL));
		int numDropsSnow = random.nextInt(getIntOption(GameOptions.NUM_DROP_SNOWBLOCK));
		World world = getWorld();
		//Drop items onto the ground
		for (int i = 0; i < numDropsSnowballs; i++) {
			Location offset = deathSpot.clone().add((random.nextInt(3) - 1)*0.5, 0, (random.nextInt(3) - 1)*0.5);
			world.dropItem(offset, fastBall);
		}
		for (int i = 0; i < numDropsSnow; i++) {
			Location offset = deathSpot.clone().add((random.nextInt(3) - 1)*0.5, 0, (random.nextInt(3) - 1)*0.5);
			world.dropItem(offset, new ItemStack(Material.SNOW_BLOCK));
		}
		world.dropItem(deathSpot.clone().add((random.nextInt(3) - 1)*0.5, 0, (random.nextInt(3) - 1)*0.5), SnowWarItemStacks.getConsumable());
	}

	public void stop() {
		//Stop active tasks
		snowManSpawnTask.cancel();
		presentTask.cancel();
		takenPresentSpots.forEach(taken -> taken.getBlock().setType(Material.AIR, true));
		//Remove snowmen
		for (ConcurrentHashMap<Location, SnowManStatus> teamSnowman : takenSnowballSpots) {
			for (SnowManStatus status : teamSnowman.values()) {
				status.getSnowman().remove();
			}
			teamSnowman.clear();
		}
		takenSnowballSpots.clear();
	}
}
