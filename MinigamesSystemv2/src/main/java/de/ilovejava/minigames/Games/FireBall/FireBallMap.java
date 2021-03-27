package de.ilovejava.minigames.Games.FireBall;

import com.google.common.collect.Sets;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.RunnableWrapper;
import de.ilovejava.minigames.Games.SnowWar.GameOptions;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.*;

public class FireBallMap extends GameMap {

	private final ArrayList<ArrayList<Location>> teamSpawns;

	@Getter
	private final ArrayList<Set<Ball>> inStasis;

	public FireBallMap(GameMap map) {
		super(map);
		int numTeams = getIntOption(GameOptions.NUM_TEAMS);
		teamSpawns = new ArrayList<>(numTeams);
		inStasis =  new ArrayList<>(numTeams);
		for (int i = 0; i < numTeams; i++) {
			teamSpawns.add(new ArrayList<>());
			inStasis.add(Sets.newConcurrentHashSet());
		}
		//Read data
		for (CustomLocation location : getLocations()) {
			if (location.containsData("SPAWN")) {
				int team = location.getIntOption("TEAM");
				teamSpawns.get(team - 1).add(location.getLocation());
			}
		}
	}

	public void respawn(Player player, int team) {
		ArrayList<Location> spawns = teamSpawns.get(team - 1);
		Location spawn = spawns.get(new Random().nextInt(spawns.size()));
		player.teleport(spawn);
	}


	private static final double phi = Math.PI * (3. - Math.sqrt(5.));

	public void summonFireballs(List<Player> active, int team) {
		int left = Math.min(getIntOption(FireBallOptions.NUM_FIREBALL) - inStasis.get(team - 1).size(), active.size());
		if (left > 0) {
			Collections.shuffle(active);
			for (int i = 0; i < left; i++) {
				Location spawn = active.get(i).getEyeLocation();
				Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(), () -> {
					double time = getDoubleOption(FireBallOptions.TIME_FIREBALL);
					Ball ball = new Ball(spawn, (int) time);
					inStasis.get(team - 1).add(ball);
					long before = (long) time;
					long rest = (long) ((time - before)*1000);
					long delay = (rest + 49)/50;
					int deathTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobby.getPlugin(), () -> {
						if (ball.next()) {
							ball.die();
							inStasis.get(team - 1).remove(ball);
						}
					}, delay, 20L);
					new RunnableWrapper(() -> Bukkit.getScheduler().cancelTask(deathTimer)).runTaskLaterAsynchronously(delay + 20L*before);
				}, 20L*3);
				int numParticles = 100;
				for (int p = 0; p < numParticles; p++) {
					double y = 1 - (p / (double) (numParticles - 1)) * 2;
					double radius = Math.sqrt(1 - y*y);
					double theta = phi * p;
					double x = Math.cos(theta) * radius;
					double z = Math.sin(theta) * radius;
					getWorld().spawnParticle(Particle.END_ROD, spawn.clone().add(x, y, z), 1, 0., 0., 0., 0.);
				}
			}
		}
	}

	public static List<Set<Player>> distributeInTeams(Collection<Player> active, int numTeams) {
		Random random = new Random();
		ArrayList<Integer> unfilledTeams = new ArrayList<>(numTeams);
		List<Set<Player>> teams = new ArrayList<>();
		for (int i = 1; i <= numTeams; i++) {
			unfilledTeams.add(i);
			teams.add(new HashSet<>());
		}
		//Players will be assigned into a team
		for (Player player : active) {
			//Refill teams for next batch
			if (unfilledTeams.isEmpty()) {
				for (int i = 1; i <= numTeams; i++) {
					unfilledTeams.add(i);
				}
			}
			//Random team for the player
			int team = unfilledTeams.remove(random.nextInt(unfilledTeams.size()));
			teams.get(team - 1).add(player);
		}
		return teams;
	}




}
