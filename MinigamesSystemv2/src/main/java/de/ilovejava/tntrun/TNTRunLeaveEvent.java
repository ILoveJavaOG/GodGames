package de.ilovejava.tntrun;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.utils.Utils;

public class TNTRunLeaveEvent extends Event implements Cancellable{
	public static HandlerList handlers = new HandlerList();
    public boolean cancelled = false;
	
    Player p;
    public TNTRunLeaveEvent(Player p) {
		this.p = p;
	}
	
    public void Leave() {
    	if(!cancelled) {
    		TNT tnt = getTNTRUnGame();
    		tnt.sendMessage("§6" + p.getName() + "§e hat das Spiel verlassen!");
    		remove();
    		p.sendMessage(Utils.getPrefix() + "§cAchtung, du hast das Laufende Spiel verlassen!");
    		Utils.getTypes().put(p, ChatType.LOBBY);
    	}
    }
    
    public void remove() {
    	TNT tnt = getTNTRUnGame();
    	if(tnt.player().contains(p)) {
    		tnt.removePlayer(p);
    	}else if(tnt.outOFGame().contains(p)) {
    		tnt.removePlayerOutOF(p);
    	}
    }
    
    public boolean isInGame() {
    	for(String tntID : Utils.TNTReady.keySet()) {
    		TNT tnt = Utils.getTNTReady().get(tntID);
    		if(tnt.player().contains(this.p) || tnt.outOFGame().contains(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public TNT getTNTRUnGame() {
    	TNT tntGame = null;
    	for(String tntID : Utils.TNTReady.keySet()) {
    		TNT tnt = Utils.getTNTReady().get(tntID);
    		if(tnt.player().contains(this.p) || tnt.outOFGame().contains(p)) {
    			tntGame = tnt;
    			break;
    		}
    	}
    	return tntGame;
    }
    
    @Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public static HandlerList getHandlerList(){
        return handlers;
	}

	@Override
	public HandlerList getHandlers() {
	        return handlers;
	}

}
