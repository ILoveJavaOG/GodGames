package de.ilovejava.minigames.Games.FastFight;

import de.ilovejava.minigames.Games.SnowWar.GameOptions;
import de.ilovejava.minigames.MapTools.CustomLocation;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FastFightMap extends GameMap {

	private final List<List<Location>> teamSpawns;

	public FastFightMap(GameMap map) {
		super(map);
		int numTeams = getIntOption(GameOptions.NUM_TEAMS);
		teamSpawns = new ArrayList<>(numTeams);
		for (int i = 0; i < numTeams; i++) {
			teamSpawns.add(new ArrayList<>());
		}
		System.out.println(getLocations());
		for (CustomLocation location : getLocations()) {
			if (location.containsData("SPAWN")) {
				Integer team = location.getIntOption("TEAM");
				assert team != null;
				teamSpawns.get(team - 1).add(location.getLocation());
			}
		}
	}

	public void spawnPlayers(Set<Player> activePlayers) {
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
			int team = unfilledTeams.remove(random.nextInt(unfilledTeams.size())) - 1;
			//Get possible spawn points for the team and move player to this point
			List<Location> possibleSpawns = teamSpawns.get(team);
			player.teleport(possibleSpawns.get(0));
		}
	}
}
