package de.ilovejava.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import de.ilovejava.Enums.ChatType;
import de.ilovejava.Enums.PVPContainer;
import de.ilovejava.lobby.Lobby;
import de.ilovejava.lottochest.Lottochest;
import de.ilovejava.mysql.API_MySQL;
import de.ilovejava.skull.SkullBase;
import de.ilovejava.skullCard.SkullCard;
import de.ilovejava.tntrun.TNT;
import de.ilovejava.user.User;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Utils {
	@Getter @Setter
	public static Lobby instance;
	@Getter @Setter
	public static API_MySQL mysql;
	@Getter @Setter
	public static String MySQlHost;
	@Getter @Setter
	public static String MySQLUser;
	@Getter @Setter
	public static String MySQLPassword;
	@Getter @Setter
	public static String MySQLDatabase;
	@Getter @Setter
	public static Integer MySQlPort;
	@Getter @Setter
	public static String Prefix;
	@Getter @Setter
	public static HashMap<String, User>User;
	@Getter @Setter
	public static HashMap<Player, ChatType>Types;
	@Getter @Setter
	public static Economy eco;
	@Getter @Setter
	public static Location spawn;
	@Getter @Setter
	public static Location vs;
	@Getter @Setter
	public static Location BedWars;
	@Getter @Setter
	public static Location Void;
	@Getter @Setter
	public static Location Ice;
	@Getter @Setter
	public static Location Fisch;
	@Getter @Setter
	public static Location Schleim;
	@Getter @Setter
	public static Location Schatz;
	@Getter @Setter
	public static ItemStack timeSkull;
	@Getter @Setter
	public static SkullBase skullbase;
	@Getter @Setter
	public static Location Shop;
	@Getter @Setter
	public static HashMap<Player, SkullCard>Skullcards;
	@Getter @Setter
	public static HashMap<String, TNT>tntcreate;
	@Getter @Setter
	public static HashMap<String, TNT>TNTReady;
	@Getter @Setter
	public static HashMap<Player, Lottochest> LottoCards;
	@Getter @Setter
	public static ArrayList<Player> waitLottochest;
	@Getter @Setter
	public static HashMap<UUID, String> nicked;
	@Getter @Setter
	public static HashMap<UUID, String> realnames;
	@Getter @Setter
	public static HashMap<UUID, String> olddisplaynames;
	@Getter @Setter
	public static HashMap<UUID, String> oldplayerlistnames;
	@Getter @Setter
	public static ArrayList<String> names;
	@Getter @Setter
	public static HashMap<GameProfile, UUID>ProfielUUIDS;
	@Getter @Setter
	public static HashMap<Player, PVPContainer> pvpStats;
	
	
	public static String getMoneyAmmount(Double ammount) {
		
        final java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        return nf.format(new BigDecimal(ammount));
    }
	
	public void playSound(String menu, String icon, Player player) {
	}
	
	public static Location setPosition(Location loc, int positionX, int positionY, double x1, double x2, double x4, double x5) {
		
		if (positionX == 1) {
			return getBFLoc(getLeftSide(loc, x1), -1.0);
		}
		if (positionX == 2) {
			return getBFLoc(getLeftSide(loc, x2), -0.5);
		}
		if (positionX == 3) {
			return getLeftSide(loc, 0);
		}
		if (positionX == 4) {
			return getBFLoc(getRightSide(loc, x4), -0.5);
		}
		if (positionX == 5) {
			return getBFLoc(getRightSide(loc, x5), -1.0);
		}
		return null;
	}
    
    /**
    * Returns a location with a specified distance away from the right side of
    * a location.
    *
    * @param location The origin location
    * @param distance The distance to the right
    * @return the location of the distance to the right
    */
    public static Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    /**
    * Gets a location with a specified distance away from the left side of a
    * location.
    *
    * @param location The origin location
    * @param distance The distance to the left
    * @return the location of the distance to the left
    */
    public static Location getLeftSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }
    
    /**
    * Gets a location with a specified distance away from the behind or front
    * from a location.
    */
    public static Location getBFLoc(Location loc, double distance) {
        Vector inverseDirectionVec = loc.getDirection().normalize().multiply(distance);
        return loc.add(inverseDirectionVec);
    } 
    
    
    public static String getNBTTag(ItemStack i) {
		net.minecraft.server.v1_12_R1.ItemStack cItem = CraftItemStack.asNMSCopy(i);
		NBTTagCompound co = (cItem.hasTag()) ? cItem.getTag() : new NBTTagCompound();
		return co.getString("arena");
	}
    
    public static ItemStack setCustomNBTTag(ItemStack i, String key, String name) {
		net.minecraft.server.v1_12_R1.ItemStack cItem = CraftItemStack.asNMSCopy(i);
		NBTTagCompound co = (cItem.hasTag()) ? cItem.getTag() : new NBTTagCompound();
		co.setString(key, name);
		cItem.setTag(co);
		return CraftItemStack.asBukkitCopy(cItem);
	}
}
