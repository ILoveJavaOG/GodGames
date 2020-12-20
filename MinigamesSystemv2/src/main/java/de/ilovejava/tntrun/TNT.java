package de.ilovejava.tntrun;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.crafttitle.API_CraftTitleApi;
import de.ilovejava.events.Event_Join;
import de.ilovejava.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TNT implements TNTRun{

	boolean isStarted  = false;
	
	private String Key;
	private ArrayList<Player> player;
	private World playWorld;
	private World lobbyWorld;
	private World specWorld;
	private Location playLoc;
	private Location lobbyLoc;
	private Location specLoc;
	private GameState state;
	private Integer min,max;
	private File f;
	private YamlConfiguration cfg;
	private String mapName;
	private int time = 60;
	private int wait = 0;
	private int gameTimer;
	private int resetTimer;
	private boolean canPlay = false;
	private boolean isStandBy = true;
	private HashMap<Location, Material>BlockMaterials;
	private ArrayList<Location> TNTReplace;
	private ArrayList<Location> SandReplace;
	private ArrayList<Player> outofGame;
	Integer replaceTimer;
	Integer timer;
	int period = 0;

	public TNT(String key) {
		this.Key = key;
		this.f = new File("plugins/Minigames/TNTRun",key+".tntrun");
		this.cfg = YamlConfiguration.loadConfiguration(this.f);
		this.player = new ArrayList<>();
	}
	
	public boolean isExsist() {
		return this.f.exists();
	}
	
	@Override
	public void create() {
		this.cfg.set("Config.Key", this.Key);
		this.cfg.set("Config.World.playworld", this.playWorld.getName());
		this.cfg.set("Config.World.Lobby", this.lobbyWorld.getName());
		this.cfg.set("Config.World.Spec", this.specWorld.getName());
		this.cfg.set("Config.Location.playworld.x", this.playLoc.getX());
		this.cfg.set("Config.Location.playworld.y", this.playLoc.getY());
		this.cfg.set("Config.Location.playworld.z", this.playLoc.getZ());
		this.cfg.set("Config.Location.playworld.yaw", this.playLoc.getYaw());
		this.cfg.set("Config.Location.playworld.pitch", this.playLoc.getPitch());
		this.cfg.set("Config.Location.Lobby.x", this.lobbyLoc.getX());
		this.cfg.set("Config.Location.Lobby.y", this.lobbyLoc.getY());
		this.cfg.set("Config.Location.Lobby.z", this.lobbyLoc.getZ());
		this.cfg.set("Config.Location.Lobby.yaw", this.lobbyLoc.getYaw());
		this.cfg.set("Config.Location.Lobby.pitch", this.lobbyLoc.getPitch());
		this.cfg.set("Config.Location.Spec.x", this.specLoc.getX());
		this.cfg.set("Config.Location.Spec.y", this.specLoc.getY());
		this.cfg.set("Config.Location.Spec.z", this.specLoc.getZ());
		this.cfg.set("Config.Location.Spec.yaw", this.specLoc.getYaw());
		this.cfg.set("Config.Location.Spec.pitch", this.specLoc.getPitch());
		this.cfg.set("Config.Player.Min", this.min);
		this.cfg.set("Config.Player.Max", this.max);
		this.cfg.set("Config.MapName", this.mapName);
		try {this.cfg.save(f);}catch(Exception e) {e.printStackTrace();}
		this.canPlay = true;
	}
	
	@Override
	public void load() {
		this.setPlayLoc(locBuilder("playworld"));
		this.setLobbyLoc(locBuilder("Lobby"));
		this.setSpecLoc(locBuilder("Spec"));
		this.setPlayWorld(this.playLoc.getWorld());
		this.setLobbyWorld(this.lobbyLoc.getWorld());
		this.setSpecWorld(this.specLoc.getWorld());
		this.setMaxPlayer(this.cfg.getInt("Config.Player.Max"));
		this.setMinPlayer(this.cfg.getInt("Config.Player.Min"));
		this.setMapName(this.cfg.getString("Config.MapName"));
		this.setState(GameState.WAIT);
		this.canPlay = true;
		Utils.getTNTReady().put(Key, this);
		this.TNTReplace = new ArrayList<>();
		this.SandReplace = new ArrayList<>();
		this.outofGame = new ArrayList<>();
		this.BlockMaterials = new HashMap<>();
	}
	
	public void save() {
		this.cfg.set("Config.Key", this.Key);
		this.cfg.set("Config.World.playworld", this.playWorld.getName());
		this.cfg.set("Config.World.Lobby", this.lobbyWorld.getName());
		this.cfg.set("Config.World.Spec", this.specWorld.getName());
		this.cfg.set("Config.Location.playworld.x", this.playLoc.getX());
		this.cfg.set("Config.Location.playworld.y", this.playLoc.getY());
		this.cfg.set("Config.Location.playworld.z", this.playLoc.getZ());
		this.cfg.set("Config.Location.playworld.yaw", this.playLoc.getYaw());
		this.cfg.set("Config.Location.playworld.pitch", this.playLoc.getPitch());
		this.cfg.set("Config.Location.Lobby.x", this.lobbyLoc.getX());
		this.cfg.set("Config.Location.Lobby.y", this.lobbyLoc.getY());
		this.cfg.set("Config.Location.Lobby.z", this.lobbyLoc.getZ());
		this.cfg.set("Config.Location.Lobby.yaw", this.lobbyLoc.getYaw());
		this.cfg.set("Config.Location.Lobby.pitch", this.lobbyLoc.getPitch());
		this.cfg.set("Config.Location.Spec.x", this.specLoc.getX());
		this.cfg.set("Config.Location.Spec.y", this.specLoc.getY());
		this.cfg.set("Config.Location.Spec.z", this.specLoc.getZ());
		this.cfg.set("Config.Location.Spec.yaw", this.specLoc.getYaw());
		this.cfg.set("Config.Location.Spec.pitch", this.specLoc.getPitch());
		this.cfg.set("Config.Player.Min", this.min);
		this.cfg.set("Config.Player.Max", this.max);
		this.cfg.set("Config.MapName", this.mapName);
		try {this.cfg.save(f);}catch(Exception e) {e.printStackTrace();}
		this.canPlay = true;
		Utils.getTNTReady().put(Key, this);
		this.TNTReplace = new ArrayList<>();
		this.SandReplace = new ArrayList<>();
		this.outofGame = new ArrayList<>();
		this.BlockMaterials = new HashMap<>();
	}
	
	private Location locBuilder(String Lockey) {
		double x,y,z;
		World w;
		Float yaw,ptich;
		
		x = this.cfg.getDouble("Config.Location."+Lockey+".x");
		y = this.cfg.getDouble("Config.Location."+Lockey+".y");
		z = this.cfg.getDouble("Config.Location."+Lockey+".z");
		yaw = (float) this.cfg.getDouble("Config.Location."+Lockey+".yaw");
		ptich = (float) this.cfg.getDouble("Config.Location."+Lockey+".pitch");
		w = Bukkit.getWorld(this.cfg.getString("Config.World."+Lockey));
		return new Location(w, x, y, z,yaw,ptich);
	}
	
	public void getBlockToRemove() {
		timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(state == GameState.RESET) {Bukkit.getScheduler().cancelTask(timer);; System.out.println("wird gelöscht!"); return;}
				for(Player p : player) {
					Location tnt = p.getLocation();
					tnt.setY(tnt.getY()-2);
					Location sand = p.getLocation();
					sand.setY(sand.getY()-1);
					destroyBlocks(tnt, sand);
					if(p.getLocation().getY() <= 0.0) {removePlayerFromGame(p);}
				}
			}
		}, 0, 1L);
	}
	
	public void removePlayerFromGame(Player p) {
		if(player.contains(p)) {
			removePlayer(p);
			p.teleport(specLoc);
			p.setGameMode(GameMode.ADVENTURE);
			outofGame.add(p);
			sendMessage("§eDer Spieler §6" + p.getName() + "§e ist ausgeschieden!");
			p.sendMessage(Utils.getPrefix() + "§cDu bist ausgeschieden!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void destroyBlocks(Location tnt, Location sand) {
		 Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if(tnt.getBlock().getType().equals(Material.TNT) && sand.getBlock().getType().equals(Material.SAND) || sand.getBlock().getType().equals(Material.GRAVEL)) {
					BlockMaterials.put(tnt, tnt.getBlock().getType());
					BlockMaterials.put(sand, sand.getBlock().getType());
					TNTReplace.add(tnt);
					SandReplace.add(sand);
					tnt.getBlock().setType(Material.AIR);
					sand.getBlock().setType(Material.AIR);
				}
			}
		}, 9);
	}
	public boolean canPlay() {
		return this.canPlay;
	}
	
	public boolean isStandBy() {
		return this.isStandBy;
	}
	
	public void setStandBy(boolean b) {
		this.isStandBy = false;
	}
	
	@Override
	public String Key() {
		return this.Key;
	}

	@Override
	public ArrayList<Player> player() {
		return this.player;
	}

	@Override
	public World playWorld() {
		return this.playWorld;
	}

	@Override
	public World Lobby() {
		return this.lobbyWorld;
	}

	@Override
	public World Spec() {
		return this.specWorld;
	}

	@Override
	public Location playLoc() {
		return this.playLoc;
	}

	@Override
	public Location lobbyLoc() {
		return this.lobbyLoc;
	}

	@Override
	public Location specLoc() {
		return this.specLoc;
	}

	@Override
	public String MapName() {
		return this.MapName();
	}

	@Override
	public GameState stat() {
		return this.state;
	}

	@Override
	public Integer Min_Player() {
		return this.min;
	}

	@Override
	public Integer Max_Player() {
		return this.max;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		gameTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if(player.size() == 0) {isStandBy = true;}
				if(isStandBy) {Bukkit.getScheduler().cancelTask(gameTimer);}
				
				switch(state) {
					case WAIT:
						if(player.size() < min) {if(wait > 60) {sendMessage("§6Warte auf Spieler: §e" + player.size()  + "§6/§e" + min); wait = 0;}else {wait++;} return;}
						setState(GameState.LOBBY);
						time = 20;
						break;
					case LOBBY:
						time--;
						setTimeXP(time);
						if(player.size() < min) {time = 120; state = GameState.WAIT; sendMessage("§cEs sind zu wenig Spieler online!");}
						if(time == 100) {sendMessage("§aDas Spiel startet in 100 Sekunden!"); playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP); return;}
						if(time == 60) {sendMessage("§aDas Spiel startet in §e60 §aSekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 30) {sendMessage("§aDas Spiel startet in §e30§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 20) {sendMessage("§aDas Spiel startet in §e20§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 10) {sendMessage("§aDas Spiel startet in §e10§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 5) {sendMessage("§aDas Spiel startet in §e5§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 4) {sendMessage("§aDas Spiel startet in §e4§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 3) {sendMessage("§aDas Spiel startet in §e3§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 2) {sendMessage("§aDas Spiel startet in §e2§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time == 1) {sendMessage("§aDas Spiel startet in §e1§a Sekunden!"); playSound(Sound.ENTITY_PLAYER_LEVELUP); return;}
						if(time <= 0) {time = 25; state = GameState.INGAME; teleportPlayerToSpawn(); setNewScoreBoard();}
						break;
					case INGAME:
						time--;
						updateScoreboard();
						setTimeXP(time);
						if(time == 100) {state = GameState.RESET;}
						if(player.size() == 1) {
							if(isStarted) {
								API_CraftTitleApi.sendFullTitle(getWinner(), 50, 50, 50, "§eGewonnen", "§bDu hast Spiel gewonnen!");
								state = GameState.RESET;
								time = 30;
							}else {
								state = GameState.WAIT;
								timer = 30;
								teleportTOGameLobby();
							}
						}
						if(time == 15) {sendMessage("§aDas Spiel startet in §e15 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 10) {sendMessage("§aDas Spiel startet in §e10 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 5) {sendMessage("§aDas Spiel startet in §e5 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 4) {sendMessage("§aDas Spiel startet in §e4 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 3) {sendMessage("§aDas Spiel startet in §e3 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 2) {sendMessage("§aDas Spiel startet in §e2 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 1) {sendMessage("§aDas Spiel startet in §e1 §aSekunden!");playSound(Sound.ENTITY_PLAYER_LEVELUP);return;}
						if(time == 0) {getBlockToRemove();time = 7200; isStarted = true;}
						break;
					case RESET:
						time--;
						setTimeXP(time);
						if(time == 20) {sendMessage("§aDas Spiel restartet in §e20 Sekunden!");}
						if(time == 15) {sendMessage("§aDas Spiel restartet in §e15 Sekunden!");}
						if(time == 10) {sendMessage("§aDas Spiel restartet in §e10 Sekunden!");}
						if(time == 5) {sendMessage("§aDas Spiel restartet in §e5 Sekunden!");}
						if(time == 4) {sendMessage("§aDas Spiel restartet in §e4 Sekunden!");}
						if(time == 3) {sendMessage("§aDas Spiel restartet in §e3 Sekunden!");}
						if(time == 2) {sendMessage("§aDas Spiel restartet in §e2 Sekunden!");}
						if(time == 1) {sendMessage("§aDas Spiel restartet in §e1 Sekunden!");}
						if(time == 0) {
							canPlay = false;
							isStandBy =  true;
							Bukkit.getScheduler().cancelTask(timer);
							teleportTOLobby();
						}
						break;
					default:
						
						break;
				}
			}
		}, 0, 20);
	}

	public void setNewScoreBoard() {
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("sss", "eee");
		
		Team over = board.registerNewTeam("Over");
		over.setPrefix("§4");
		over.setSuffix("§f0");
		over.addEntry(ChatColor.BLACK.toString());
		
		Team self = board.registerNewTeam("Self");
		self.setPrefix("§c");
		self.setSuffix("§f"+player.size());
		self.addEntry(ChatColor.BOLD.toString());
		
		Team under = board.registerNewTeam("Under");
		under.setPrefix("§4");
		under.setSuffix("§f0");
		under.addEntry(ChatColor.BLUE.toString());
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("§6TNTRun");
		obj.getScore("§6Spieler über dir").setScore(16);
		obj.getScore(ChatColor.BLACK.toString()).setScore(15);
		obj.getScore(" ").setScore(14);
		obj.getScore("§6Spieler neben dir").setScore(13);
		obj.getScore(ChatColor.BOLD.toString()).setScore(12);
		obj.getScore("  ").setScore(11);
		obj.getScore("§6Spieler unter dir").setScore(10);
		obj.getScore(ChatColor.BLUE.toString()).setScore(9);
		for(Player p : player) {
			p.setScoreboard(board);
		}
	}
	
	public void updateScoreboard() {
		if(state.equals(GameState.INGAME)) {
			for(Player p : player) {
				int over = getUpperPlayer(p);
				int under = getLowerPlayer(p);
				int remove = player.size()-under-over-1;
				p.getScoreboard().getTeam("Over").setSuffix("§f"+over);
				p.getScoreboard().getTeam("Self").setSuffix("§f"+remove);
				p.getScoreboard().getTeam("Under").setSuffix("§f"+under);
			}
		}
	}
	
	public Integer getLowerPlayer(Player p) {
		Integer i = 0;
		Location loc = p.getLocation();
		loc.setY(loc.getY()-2.0);
		
		for(Player c : player) {
			if(p != c) {
				if(c.getLocation().getY() < loc.getY()) {
					i++;
				}
			}
		}
		return i;
	}
	
	public Integer getUpperPlayer(Player p) {
		Integer i = 0;
		Location loc = p.getLocation();
		loc.setY(loc.getY()+2);
		for(Player c : player) {
			if(c != p ) {
				if(c.getLocation().getY() > loc.getY()) {
					i++;
				}
			}
		}
		return i;
	}
	
	public Player getWinner() {
		Player p = null;
		for(Player t : player) {
			if(t != null) {
				p = t;
				break;
			}
		}
		return p;
	}
	
	public void teleportTOLobby() {
		for(Player p : player) {p.teleport(Utils.getSpawn()); new de.ilovejava.scoreboard.Scoreboard(p); Utils.getTypes().put(p, ChatType.LOBBY); Event_Join.giveLobbyItems(p);}
		for(Player p : outofGame) {p.teleport(Utils.getSpawn()); new de.ilovejava.scoreboard.Scoreboard(p); Utils.getTypes().put(p, ChatType.LOBBY); Event_Join.giveLobbyItems(p);}
		this.player.clear();
		this.outofGame.clear();
		this.reset();
	}
	
	public void teleportPlayerToSpawn() {
		for(Player p : player) {
			p.teleport(this.playLoc);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void reset() {
		resetTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				if(period >= TNTReplace.size()) {
					Bukkit.getScheduler().cancelTask(resetTimer);
					period = 0;
					replacesand();
					return;
				}else {
					TNTReplace.get(period).getBlock().setType(BlockMaterials.get(TNTReplace.get(period)));
					period++;
					return;
					
				}
			}
		}, 0, 1);
	}

	public void teleportTOGameLobby() {
		for(Player p : player) {
			p.teleport(this.lobbyLoc);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void replacesand() {
		replaceTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if(period >= SandReplace.size()) {
					Bukkit.getScheduler().cancelTask(resetTimer);
					period = 0;
					resetGameStates();
					return;
				}else {
					SandReplace.get(period).getBlock().setType(BlockMaterials.get(SandReplace.get(period)));
					period ++;
					return;
				}
			}
		}, 0, 1);
	}
	
	public void resetGameStates() {
		this.state = GameState.WAIT;
		this.canPlay = true;
		this.TNTReplace.clear();
		this.SandReplace.clear();
		this.BlockMaterials.clear();
	}
	
	public void setTimeXP(int leve) {
		for(Player p : player) {p.setLevel(leve);}
		for(Player p : outofGame) {p.setLevel(leve);}
	}
	
	@Override
	public void sendMessage(String msg) {
		for(Player p : player) {p.sendMessage(Utils.getPrefix() + msg);}
		for(Player p : outofGame) {p.sendMessage(Utils.getPrefix() + msg);}
	}

	public void playSound(Sound s){
		for(Player p : player) {
			p.playSound(p.getLocation(), s, 1, 1);
		}
	}
	
	@Override
	public void setKey(String key) {
		this.Key = key;
	}

	public ArrayList<Player> outOFGame(){
		return this.outofGame;
	}
	
	public void removePlayerOutOF(Player p) {
		this.outofGame.remove(p);
	}
	
	@Override
	public void setPlayer(ArrayList<Player> list) {
		this.player = list;
	}

	@Override
	public void addPlayer(Player p) {
		this.player.add(p);
	}

	@Override
	public void removePlayer(Player p) {
		this.player.remove(p);
	}

	@Override
	public void setPlayWorld(World w) {
		this.playWorld = w;
	}

	@Override
	public void setLobbyWorld(World w) {
		this.lobbyWorld = w;
	}

	@Override
	public void setSpecWorld(World w) {
		this.specWorld = w;
	}

	@Override
	public void setPlayLoc(Location l) {
		this.playLoc = l;
	}

	@Override
	public void setLobbyLoc(Location l) {
		this.lobbyLoc = l;
	}

	@Override
	public void setSpecLoc(Location l) {
		this.specLoc = l;
	}

	@Override
	public void setMapName(String n) {
		this.mapName = n;
	}

	@Override
	public void setState(GameState s) {
		this.state = s;
	}

	@Override
	public void setMinPlayer(Integer min) {
		this.min = min;
	}

	@Override
	public void setMaxPlayer(Integer max) {
		this.max = max;
	}

	@Override
	public String mapName() {
		return this.mapName;
	}

}
