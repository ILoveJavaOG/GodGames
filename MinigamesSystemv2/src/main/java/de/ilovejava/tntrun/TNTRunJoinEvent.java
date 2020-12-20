package de.ilovejava.tntrun;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.Enums.PVPContainer;
import de.ilovejava.tdg.TDG;
import de.ilovejava.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TNTRunJoinEvent extends Event implements Cancellable{

	public static HandlerList handlers = new HandlerList();
    public boolean cancelled = false;
    
    Player p;
    TNT tnt;
    
    public TNTRunJoinEvent(Player p, TNT tnt) {
		this.p = p;
		this.tnt = tnt;
		if(Utils.getInstance().pmenu.containsKey(p)) {
			TDG tdg = Utils.getInstance().pmenu.get(p);
			tdg.closeMenu(p);
		}
	}
	
    public Player getPlayer() {
    	return this.p;
    }
    
    public TNT getTNT() {
    	return tnt;
    }
    
    public void checkStandBy() {
    	if(tnt.isStandBy()) {
    		tnt.run();
    		tnt.setStandBy(false);
    	}
    }
    
    public void sendJoinMessage() {
    	if(!cancelled) {
    		tnt.sendMessage("§6" + p.getName() + "§e hat das Spiel betreten!");
    		Utils.getTypes().put(p, ChatType.INGAME);
    		Utils.getPvpStats().put(p, PVPContainer.PVPNONFOOD);
    		p.getInventory().clear();
    	}
    }
    
    public void teleport() {
    	if(!cancelled) {
    		p.teleport(tnt.lobbyLoc());
    	}
    }
    
    public void sendNotJoin() {
    	p.sendMessage(Utils.getPrefix() + "§cLeider ist das Spiel nicht mehr öffentlich!");
    }
    
    public boolean canJoin() {
    	if(!cancelled) {
    		if(tnt.player().size() <= tnt.Max_Player()) {
    			if(tnt.stat().equals(GameState.WAIT) || tnt.stat().equals(GameState.LOBBY)){
    				return true;
    			}
    			return false;
    		}else {return false;}
    	}else {return false;}
    }
    
    public boolean canPlay() {
    	return tnt.canPlay();
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
