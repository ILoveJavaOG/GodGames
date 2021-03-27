package de.ilovejava.minigames.Games.FishFight;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Calendar;

/**
 * Class to handle fishing state for player
 */
public class FishingStatus {

	//Currently assigned auto bite task
	private final BukkitTask task;

	//Start of bite
	private final long start = Calendar.getInstance().getTimeInMillis();

	//Last known state
	@Getter
	@Setter
	private PlayerFishEvent.State lastState;

	/**
	 * Constructor for the status
	 * @param lastState(PlayerFishEvent.State): Last known state
	 * @param task
	 */
	public FishingStatus(BukkitTask task, PlayerFishEvent.State lastState) {
		this.task = task;
		this.lastState = lastState;
	}

	/**
	 * Utility method to stop the auto bite task
	 */
	public void stop() {
		task.cancel();
	}

	/**
	 * Method to determine if hook was successful
	 *
	 * @return True if time between hook and bite is less than 300 millis
	 */
	public boolean isCaught() {
		return Calendar.getInstance().getTimeInMillis() - start <= 300;
	}
}
