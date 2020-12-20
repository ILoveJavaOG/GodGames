package de.ilovejava.minigames.Games.KOFTH;

import de.ilovejava.minigames.GameLogic.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Class to handle events during the fish fight
 */
public class KOFTHEvents implements Events {

	public void onMove(PlayerMoveEvent event) {
		Player moving = event.getPlayer();
		double height = moving.getLocation().getY();
		int level = (int) height;
		double fractionalHeight = height - level;
		moving.setLevel(level);
		moving.setExp((float) fractionalHeight);
	}
}
