package de.ilovejava.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;

import de.ilovejava.Enums.PVPContainer;
import de.ilovejava.autoinit.TNTRun;
import de.ilovejava.commands.Command_Nick;
import de.ilovejava.commands.Command_Spawn;
import de.ilovejava.commands.Command_TnTRun;
import de.ilovejava.events.Event_ArmorStand;
import de.ilovejava.events.Event_BlockChange;
import de.ilovejava.events.Event_Build;
import de.ilovejava.events.Event_Chat;
import de.ilovejava.events.Event_CreatureSpawn;
import de.ilovejava.events.Event_Damge;
import de.ilovejava.events.Event_Drop;
import de.ilovejava.events.Event_Food;
import de.ilovejava.events.Event_InterAcct;
import de.ilovejava.events.Event_InterActWithEntity;
import de.ilovejava.events.Event_InventoryClick;
import de.ilovejava.events.Event_InventoryClose;
import de.ilovejava.events.Event_Join;
import de.ilovejava.events.Event_Leave;
import de.ilovejava.events.Event_PickUpItem;
import de.ilovejava.events.Event_TDGSelector;
import de.ilovejava.events.Event_TNTSelectorClick;
import de.ilovejava.events.Event_TeamClick;
import de.ilovejava.events.Event_WeahtChange;
import de.ilovejava.lottochest.Lottochest;
import de.ilovejava.mysql.InsertTypes;
import de.ilovejava.scoreboard.Scoreboard;
import de.ilovejava.shop.Shop;
import de.ilovejava.skull.SkullBase;
import de.ilovejava.tdg.Skull;
import de.ilovejava.tdg.TDG;
import de.ilovejava.tntrun.TNTEvents;
import de.ilovejava.utils.Utils;
import de.ilovejava.vault.VaultConnector;
import de.lovejava.autoinit.LoadSkulls;
import de.lovejava.autoinit.LoadUser;
import net.milkbowl.vault.economy.Economy;

public class Lobby extends JavaPlugin{
	
	private static Economy econ = null;
	
	public String ver = this.getDescription().getVersion();
	public boolean papi = false;
	
	public List<Entity> entities = new ArrayList<Entity>();
	public HashMap<Player, TDG> pmenu = new HashMap<Player, TDG>();
	public HashMap<Player, Location> lastLoc = new HashMap<Player, Location>();
	
	@Override
	public void onEnable() {
		removeEntety();
		
		Utils.setInstance(this);
		Utils.setUser(new HashMap<>());
		Utils.setTypes(new HashMap<>());
		Utils.setTimeSkull(Skull.getSkull("http://textures.minecraft.net/texture/f37cae5c51eb1558ea828f58e0dff8e6b7b0b1a183d737eecf714661761"));
		Utils.setSkullbase(new SkullBase());
		Utils.setSkullcards(new HashMap<>());
		Utils.setTntcreate(new HashMap<>());
		Utils.setTNTReady(new HashMap<>());
		Utils.setWaitLottochest(new ArrayList<Player>());
		Utils.setLottoCards(new HashMap<Player, Lottochest>());
		Utils.setNicked(new HashMap<UUID, String>());
		Utils.setRealnames(new HashMap<UUID, String>());
		Utils.setOlddisplaynames(new HashMap<UUID, String>());
		Utils.setOldplayerlistnames(new HashMap<UUID, String>());
		Utils.setNames(new ArrayList<String>());
		Utils.setProfielUUIDS(new HashMap<GameProfile, UUID>());
		Utils.setPvpStats(new HashMap<Player, PVPContainer>());
		new TNTRun();
		setUpConfig();
		loadEvent();
		loadCommands();
		new InsertTypes();
		new LoadUser();
		new LoadSkulls();
		new Shop();
		setupEconomy();
		setupEco();
		Utils.setEco(econ);
		Scoreboard.update();
		setNickNames();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void setNickNames() {
		Utils.getNames().add("TooMuchSun");
		Utils.getNames().add("XPaulistoX");
		Utils.getNames().add("XxDerHenkerxX");
		Utils.getNames().add("KuchenFNA_YT");
		Utils.getNames().add("Konnian");
		Utils.getNames().add("Meisterwerk");
		Utils.getNames().add("zF4b");
		Utils.getNames().add("Durchgedrehter");
		Utils.getNames().add("DrMini");
		Utils.getNames().add("Enttaeuschte");
		Utils.getNames().add("PandaPl4ysTV");
		Utils.getNames().add("Schnicks");
		Utils.getNames().add("EndLand");
		Utils.getNames().add("DieserName");
		Utils.getNames().add("WaifuAsuna");
		Utils.getNames().add("Marv1n");
		Utils.getNames().add("hardcorelucky8");
		Utils.getNames().add("KobrasGirl");
		Utils.getNames().add("9FredTV");
		Utils.getNames().add("FxznOP");
		Utils.getNames().add("ItzJuliiYT");
	    Utils.getNames().add("DerPedoMichi");
	    Utils.getNames().add("SchinkenLP");
	    Utils.getNames().add("SnailYT");
	    Utils.getNames().add("zebbe9999");
	    Utils.getNames().add("ukcats");
	    Utils.getNames().add("x_kyle_x");
	    Utils.getNames().add("wolpaladin");
	    Utils.getNames().add("speeeder86");
	    Utils.getNames().add("tapout10");
	    Utils.getNames().add("stickzer0");
	    Utils.getNames().add("sureynot");
	    Utils.getNames().add("waltisawesome");
	    Utils.getNames().add("timperi");
	    Utils.getNames().add("shak1145");
	    Utils.getNames().add("theSero");
	    Utils.getNames().add("thetangledhand");
	    Utils.getNames().add("wildii");
	    Utils.getNames().add("wilde_katze");
	    Utils.getNames().add("viking58");
	    Utils.getNames().add("timii");
	    Utils.getNames().add("rumblefish");
	    Utils.getNames().add("pigman21");
	    Utils.getNames().add("TauFirewarrior");
	    Utils.getNames().add("Tavorrik");
	    Utils.getNames().add("tcassel9898");
	    Utils.getNames().add("TehTeNdEnCiEs");
	    Utils.getNames().add("tenshispawn");
	    Utils.getNames().add("terrorist_109");
	    Utils.getNames().add("tesla2000");
	    Utils.getNames().add("tgy320");
	    Utils.getNames().add("Th1Alchemyst");
	    Utils.getNames().add("tharcon");
	}
	
	private void removeEntety() {
		int i = 0;
		for(World w : Bukkit.getWorlds()) {
			w.setStorm(false);
			for(Entity e : w.getEntities()) {
				if(!e.getType().equals(EntityType.PLAYER)) {
					e.remove();
					i++;
				}
			}
		}
		Bukkit.getConsoleSender().sendMessage("§b" + i + "Entitys gelöscht!");
	}
	
	private void loadCommands() {
		new Command_Spawn("spawn",this);
		new Command_TnTRun("tntrun",this);
		new Command_Nick("Nick",this);
	}
	
	private void setupEconomy() {
		this.getServer().getServicesManager().register(Economy.class, new VaultConnector(), Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);
	}
	
	private void loadEvent() {
		HashSet<Listener> events = new HashSet<>();
		events.add(new Event_Join());
		events.add(new Event_Leave());
		events.add(new Event_Chat());
		events.add(new Event_Damge());
		events.add(new Event_Food());
		events.add(new Event_Build());
		events.add(new Event_Drop());
		events.add(new Event_InterAcct());
		events.add(new Event_ArmorStand());
		events.add(new Event_WeahtChange());
		events.add(new Event_CreatureSpawn());
		events.add(new Event_PickUpItem());
		events.add(new Event_TDGSelector());
		events.add(new Event_InterActWithEntity());
		events.add(new Event_InventoryClick());
		events.add(new Event_InventoryClose());
		events.add(new TNTEvents());
		events.add(new Event_BlockChange());
		events.add(new Event_TNTSelectorClick());
		events.add(new Event_TeamClick());
		for(Listener l : events) {
			Bukkit.getServer().getPluginManager().registerEvents(l, this);
		}
	}
	
	private boolean setupEco() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	System.out.println("Vault wurde nicht gefunden!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	System.out.println("Vault wurde nicht gefunden! 1");
            return false;
        }
        
        System.out.println("Vault wurde gefunden!");
        
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private void setUpConfig() {
		isSet("Config.MySQL.Host", "Host");
		isSet("Config.MySQL.User", "User");
		isSet("Config.MySQL.Password", "Password");
		isSet("Config.MySQL.Database", "Minigames");
		isSet("Config.MySQL.port", 3306);
		
		FileConfiguration cfg = this.getConfig();
		Utils.setMySQlHost(cfg.getString("Config.MySQL.Host"));
		Utils.setMySQLUser(cfg.getString("Config.MySQL.User"));
		Utils.setMySQLPassword(cfg.getString("Config.MySQL.Password"));
		Utils.setMySQLDatabase(cfg.getString("Config.MySQL.Database"));
		Utils.setMySQlPort(cfg.getInt("Config.MySQL.port"));
		Utils.setPrefix("§b§o§lArdnarun§8: §f");
		
		if(cfg.isSet("Config.Spawn.X")) {
			Double x = cfg.getDouble("Config.Spawn.X");
			Double y = cfg.getDouble("Config.Spawn.Y");
			Double z = cfg.getDouble("Config.Spawn.Z");
			Float yaw = (float) cfg.getDouble("Config.Spawn.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Spawn.Pitch");
			String world = cfg.getString("Config.Spawn.World");
			Utils.setSpawn(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.VS.X")) {
			Double x = cfg.getDouble("Config.VS.X");
			Double y = cfg.getDouble("Config.VS.Y");
			Double z = cfg.getDouble("Config.VS.Z");
			Float yaw = (float) cfg.getDouble("Config.VS.Yaw");
			Float pitch = (float) cfg.getDouble("Config.VS.Pitch");
			String world = cfg.getString("Config.VS.World");
			Utils.setVs(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein 1vs1 spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.BW.X")) {
			Double x = cfg.getDouble("Config.BW.X");
			Double y = cfg.getDouble("Config.BW.Y");
			Double z = cfg.getDouble("Config.BW.Z");
			Float yaw = (float) cfg.getDouble("Config.BW.Yaw");
			Float pitch = (float) cfg.getDouble("Config.BW.Pitch");
			String world = cfg.getString("Config.BW.World");
			Utils.setBedWars(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein BedWars spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.Void.X")) {
			Double x = cfg.getDouble("Config.Void.X");
			Double y = cfg.getDouble("Config.Void.Y");
			Double z = cfg.getDouble("Config.Void.Z");
			Float yaw = (float) cfg.getDouble("Config.Void.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Void.Pitch");
			String world = cfg.getString("Config.Void.World");
			Utils.setVoid(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Void Run spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.Ice.X")) {
			Double x = cfg.getDouble("Config.Ice.X");
			Double y = cfg.getDouble("Config.Ice.Y");
			Double z = cfg.getDouble("Config.Ice.Z");
			Float yaw = (float) cfg.getDouble("Config.Ice.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Ice.Pitch");
			String world = cfg.getString("Config.Ice.World");
			Utils.setIce(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Ice spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.Fisch.X")) {
			Double x = cfg.getDouble("Config.Fisch.X");
			Double y = cfg.getDouble("Config.Fisch.Y");
			Double z = cfg.getDouble("Config.Fisch.Z");
			Float yaw = (float) cfg.getDouble("Config.Fisch.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Fisch.Pitch");
			String world = cfg.getString("Config.Fisch.World");
			Utils.setFisch(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Fisch spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.Schleim.X")) {
			Double x = cfg.getDouble("Config.Schleim.X");
			Double y = cfg.getDouble("Config.Schleim.Y");
			Double z = cfg.getDouble("Config.Schleim.Z");
			Float yaw = (float) cfg.getDouble("Config.Schleim.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Schleim.Pitch");
			String world = cfg.getString("Config.Schleim.World");
			Utils.setSchleim(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Schleim spawn gesetzt werden!");}
		
		if(cfg.isSet("Config.Schatz.X")) {
			Double x = cfg.getDouble("Config.Schatz.X");
			Double y = cfg.getDouble("Config.Schatz.Y");
			Double z = cfg.getDouble("Config.Schatz.Z");
			Float yaw = (float) cfg.getDouble("Config.Schatz.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Schatz.Pitch");
			String world = cfg.getString("Config.Schatz.World");
			Utils.setSchatz(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}else {Bukkit.getConsoleSender().sendMessage("§cAchtung, es muss ein Schatz spawn gesetzt werden!");}
		if(cfg.isSet("Config.Shop.X")) {
			Double x = cfg.getDouble("Config.Shop.X");
			Double y = cfg.getDouble("Config.Shop.Y");
			Double z = cfg.getDouble("Config.Shop.Z");
			Float yaw = (float) cfg.getDouble("Config.Shop.Yaw");
			Float pitch = (float) cfg.getDouble("Config.Shop.Pitch");
			String world = cfg.getString("Config.Shop.World");
			Utils.setShop(new Location(Bukkit.getWorld(world), x, y, z,yaw,pitch));
		}
	}
	
	private void isSet(String key, Object obj){
		if(!this.getConfig().isSet(key)) {
			this.getConfig().set(key, obj);
			this.saveConfig();
		}
	}
}
