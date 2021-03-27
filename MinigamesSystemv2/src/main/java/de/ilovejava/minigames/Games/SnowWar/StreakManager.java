package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.lobby.Lobby;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.KillStreakCost;
import de.ilovejava.minigames.Games.SnowWar.HelperClasses.PlayerStats;
import de.ilovejava.minigames.MapTools.GameMap;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static de.ilovejava.minigames.Games.SnowWar.SnowWarItemStacks.*;

public class StreakManager {

	//List of the available streaks sorted by the requirement
	private final KillStreakCost[] streakRequirements = new KillStreakCost[3];

	//Hashmap containing the stats for the player
	final ConcurrentHashMap<Player, PlayerStats> playerData = new ConcurrentHashMap<>();

	public StreakManager(GameMap map) {
		KillStreak[] streaks = KillStreak.values();
		for (int i = 0; i < streaks.length; i++) {
			Integer requirement = map.getIntOption(streaks[i].name());
			streakRequirements[i] = new KillStreakCost(streaks[i], requirement);
		}
	}

	/**
	 * Method to calculate the streak progress for the player
	 *
	 * @param source(Player): Player to get progress for
	 */
	public void setStreakProgress(Player source) {
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
				source.playSound(source.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
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

	public void resetStreak(Player deadPlayer) {
		playerData.computeIfPresent(deadPlayer, (player, stats) -> new PlayerStats(stats.getHits(), 0, stats.getTeam()));
	}

	/**
	 * Method to be called when a player is activating his sonar
	 *
	 * @param player(Player): Player who activated the streak
	 */
	protected void activateSonar(Player player, Set<Player> activePlayers, long sonarTime) {
		//Remove streak
		player.getInventory().setItem(0, sonarItemPlaceHolder);
		//Get the team of the person activating the streak
		int team = playerData.get(player).getTeam();
		//Make other teams glowing and not glowing
		for (Player active : activePlayers) {
			active.playSound(active.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 1f, 1.5f);
			if (playerData.get(active).getTeam() != team) active.setGlowing(true);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.getPlugin(),
				() -> activePlayers.forEach(playing -> {
					playing.playSound(playing.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1f, 1f);
					playing.setGlowing(false);
				}), 20L*sonarTime);
	}

	/**
	 * Method to be called when a player is activating a supply drop
	 *
	 * @param player(Player): Player who activated the streak
	 * @param dropSpot(Location): Location of where to activate the streak
	 */
	protected void activateSupplyDrop(Player player, Location dropSpot) {
		World world = dropSpot.getWorld();
		assert world != null;
		world.playSound(dropSpot, Sound.BLOCK_BEACON_ACTIVATE, 3f, 1.0f);
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
		//Throw ender pearl
		bomber.launchProjectile(EnderPearl.class);
		//Reset streak
		playerData.computeIfPresent(bomber, (player, stats) -> new PlayerStats(stats.getHits(), 0, stats.getTeam()));
		bomber.setExp(0.0f);
	}
}
