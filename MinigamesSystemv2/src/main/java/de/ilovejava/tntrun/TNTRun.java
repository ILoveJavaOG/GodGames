package de.ilovejava.tntrun;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface TNTRun {
	public String Key();
	public ArrayList<Player>player();
	public World playWorld();
	public World Lobby();
	public World Spec();
	public Location playLoc();
	public Location lobbyLoc();
	public Location specLoc();
	public String MapName();
	public GameState stat();
	public Integer Min_Player();
	public Integer Max_Player();
	public String mapName();
	public void run();
	public void reset();
	public void sendMessage(String msg);
	
	public void load();
	public void create();
	
	public void setKey(String key);
	public void setPlayer(ArrayList<Player> list);
	public void addPlayer(Player p);
	public void removePlayer(Player p);
	public void setPlayWorld(World w);
	public void setLobbyWorld(World w);
	public void setSpecWorld(World w);
	public void setPlayLoc(Location l);
	public void setLobbyLoc(Location l);
	public void setSpecLoc(Location l);
	public void setMapName(String n);
	public void setState(GameState s);
	public void setMinPlayer(Integer min);
	public void setMaxPlayer(Integer max);
}
