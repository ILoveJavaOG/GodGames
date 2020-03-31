package de.ilovejava.tntrun;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TNTEvents implements Listener{
	@EventHandler
	public void tntjoin(TNTRunJoinEvent e) {
		if(e.canJoin()) {
			e.teleport();
			e.getTNT().addPlayer(e.getPlayer());
			e.checkStandBy();
			e.sendJoinMessage();
		}else {e.sendNotJoin(); e.setCancelled(true);}
	}
	
	@EventHandler
	public void tntleave(TNTRunLeaveEvent e) {
		if(e.isInGame()) {
			e.Leave();
		}else {e.setCancelled(true);}
	}
}
