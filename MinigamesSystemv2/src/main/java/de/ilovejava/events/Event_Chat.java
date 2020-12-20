package de.ilovejava.events;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Event_Chat implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		ChatType ct = Utils.getTypes().get(e.getPlayer());
		switch(ct) {
			case INGAME:
				
				break;
			case LOBBY:
				writeLobby(e.getMessage(), e.getPlayer().getDisplayName(),e.getPlayer());
				break;
			case SPECTATOR:
				
				break;
			case SUPPORT:
				
				break;
		}
	}
	
	private void writeLobby(String message, String sender, Player send) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(Utils.getTypes().get(p).equals(ChatType.LOBBY)) {
				if(send.hasPermission("GG.Admin")) {
					p.sendMessage("§c"+sender+"§8>>§f " +message);
				}else if(send.hasPermission("GG.Build")) {
					p.sendMessage("§e"+sender+"§8>>§f " +message);
				}else if(send.hasPermission("GG.Support")) {
					p.sendMessage("§6"+sender+"§8>>§f " +message);
				}else if(send.hasPermission("GG.Team")){
					p.sendMessage("§9"+sender+"§8>>§f " +message);
				}else {
					p.sendMessage("§a"+sender+"§8>>§f " +message);
				}
			}
		}
	}
}
