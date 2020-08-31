package de.ilovejava.events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mojang.authlib.GameProfile;

import de.ilovejava.tntrun.TNTRunLeaveEvent;
import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;

public class Event_Leave implements Listener {
	@EventHandler
	public void onleave(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		
		Bukkit.getPluginManager().callEvent(new TNTRunLeaveEvent(e.getPlayer()));
		
		String uuid = uuidfetcher.getUUID(e.getPlayer().getName()).toString();
		User s = Utils.getUser().get(uuid);
		s.setOnline(false);
		s.save();
	
		s.setToRemoveArden(0.0);
		Utils.getTypes().remove(e.getPlayer());
		
		GameProfile gp = ((CraftPlayer) e.getPlayer()).getHandle().getProfile();
		if(Utils.getProfielUUIDS().containsKey(gp)) {
			Utils.getProfielUUIDS().remove(gp);
			Utils.olddisplaynames.remove(e.getPlayer().getUniqueId());
			Utils.oldplayerlistnames.remove(e.getPlayer().getUniqueId());
			Utils.realnames.remove(e.getPlayer().getUniqueId());
			Utils.nicked.remove(e.getPlayer().getUniqueId());
		}
	}
}
