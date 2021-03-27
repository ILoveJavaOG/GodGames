package de.ilovejava.minigames.Games.FastFight;

import de.ilovejava.minigames.Abilities.Ability;
import de.ilovejava.minigames.Communication.IsUsed;
import de.ilovejava.minigames.GameLogic.Events;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * Class to handle events during the fish fight
 */
public class FastFightEvents implements Events {

	@IsUsed
	public void onSwitch(PlayerItemHeldEvent event) {
		int selected = event.getNewSlot();
		if (selected <= 2) {
			event.setCancelled(true);
			Ability.current.get(event.getPlayer())[selected].activate();
		}
	}

}
