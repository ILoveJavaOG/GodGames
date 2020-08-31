
package de.ilovejava.support.skin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NickSystem {
	@SuppressWarnings("deprecation")
	public static void nickPlayer(Player p, String nickname){
		UUID uuid = uuidfetcher.getUUID(p.getName());
		Utils.olddisplaynames.put(p.getUniqueId(), p.getDisplayName());
		Utils.oldplayerlistnames.put(p.getUniqueId(), p.getPlayerListName());
		
		Utils.realnames.put(p.getUniqueId(), p.getName());
		Utils.nicked.put(p.getUniqueId(), nickname);
		
		nickname = ChatColor.translateAlternateColorCodes('&', nickname);
		
		try{
			
			Method getHandle = p.getClass().getMethod("getHandle", new Class[0]);
			Object profile = getHandle.invoke(p, new Object[0]).getClass().getMethod("getProfile", new Class[0]).invoke(getHandle.invoke(p, new Object[0]), new Object[0]);
			Field f = profile.getClass().getDeclaredField("name");
			f.setAccessible(true);
			f.set(profile, nickname);
			
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		
		MinecraftServer nmsServer = ((CraftServer)Bukkit.getServer()).getServer();
		WorldServer nmsWelt = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
		
		EntityPlayer ep = ((CraftPlayer)p).getHandle();
		EntityPlayer pNeu = new EntityPlayer(nmsServer, nmsWelt, new GameProfile(p.getUniqueId(), nickname), new PlayerInteractManager(nmsWelt));
		
		for(Player all : Bukkit.getOnlinePlayers()){
			
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep }));
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { pNeu }));
			
			if(all != p){
		        ((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(ep));
			}
		}
		
		GameProfile gp = ((CraftPlayer)p).getProfile();
		gp.getProperties().clear();
		GameProfile skinGF = null;
		try {skinGF = GameProfileBuilder.fetch(uuidfetcher.getUUID(nickname));}catch(Exception e) {e.printStackTrace();}
		Collection<Property> props = skinGF.getProperties().get("textures");
		gp.getProperties().putAll("textures", props);		
			
		for(Player all : Bukkit.getOnlinePlayers()){
			
			all.hidePlayer(p);
			all.showPlayer(p);
			
		}
		p.setDisplayName(nickname);
		p.setPlayerListName(nickname);
		Utils.ProfielUUIDS.put(((CraftPlayer)p).getHandle().getProfile(), uuid);
	}
	
	@SuppressWarnings("deprecation")
	public static void unnickPlayer(Player p){
		GameProfile gep = ((CraftPlayer) p).getHandle().getProfile();
		String realname = getRealName(p);
		
		try{
			
		    Method getHandle = p.getClass().getMethod("getHandle", new Class[0]);
		    Object profile = getHandle.invoke(p, new Object[0]).getClass().getMethod("getProfile", new Class[0]).invoke(getHandle.invoke(p, new Object[0]), new Object[0]);
		    Field f = profile.getClass().getDeclaredField("name");
		    f.setAccessible(true);
		    f.set(profile, realname);
			
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		
		MinecraftServer nmsServer = ((CraftServer)Bukkit.getServer()).getServer();
		WorldServer nmsWelt = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
		
		EntityPlayer ep = ((CraftPlayer)p).getHandle();
		EntityPlayer pNeu = new EntityPlayer(nmsServer, nmsWelt, new GameProfile(p.getUniqueId(), realname), new PlayerInteractManager(nmsWelt));
		
		
		for(Player all : Bukkit.getOnlinePlayers()){
			
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep }));
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { pNeu }));
			
			if(all != p){
		        ((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(ep));
			}
		}
		GameProfile gp = ((CraftPlayer)p).getProfile();
		gp.getProperties().clear();
		
		GameProfile skinGF = null;
		try {skinGF = GameProfileBuilder.fetch(uuidfetcher.getUUID(Utils.getRealnames().get(p.getUniqueId())));}catch(Exception e) {e.printStackTrace();}
		Collection<Property> props = skinGF.getProperties().get("textures");
		gp.getProperties().putAll("textures", props);		
		
		for(Player all : Bukkit.getOnlinePlayers()){
			
			all.hidePlayer(p);
			all.showPlayer(p);
			
		}
		
		p.setDisplayName(getOldDisplayName(p));
		p.setPlayerListName(getOldPlayerListName(p));
		
		Utils.realnames.remove(p.getUniqueId(), getRealName(p));
		Utils.nicked.remove(p.getUniqueId(), getNickName(p));
		Utils.olddisplaynames.remove(p.getUniqueId(), getOldDisplayName(p));
		Utils.oldplayerlistnames.remove(p.getUniqueId(), getOldPlayerListName(p));
		Utils.getProfielUUIDS().remove(gep);
	}
	
	public static String getRealName(Player p){
		return (String)Utils.realnames.get(p.getUniqueId());
	}
	
	public static String getNickName(Player p){
		return (String)Utils.nicked.get(p.getUniqueId());
	}
	
	public static String getOldDisplayName(Player p){
		return (String)Utils.olddisplaynames.get(p.getUniqueId());
	}
	
	public static String getOldPlayerListName(Player p){
		return (String)Utils.oldplayerlistnames.get(p.getUniqueId());
	}
	
	public static String getRandomName(){
		Random r = new Random();
		return (String)Utils.names.get(r.nextInt(Utils.names.size()));
	}
	
	public static String getRandomNameFromArrayList(ArrayList<String> list){
		Random r = new Random();
		return (String)list.get(r.nextInt(list.size()));
	}
	
	  public static void setChatNamePrefix(Player p, String chatPrefix){
	    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', chatPrefix) + p.getDisplayName());
	  }
	  
	  public static void setTabNamePrefix(Player p, String tabPrefix){
	    p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', tabPrefix) + p.getDisplayName());
	  }
	  
	  public static void setChatNameSuffix(Player p, String chatSuffix){
	    p.setDisplayName(p.getDisplayName() + ChatColor.translateAlternateColorCodes('&', chatSuffix));
	  }
	  
	  public static void setTabNameSuffix(Player p, String tabSuffix){
	    p.setPlayerListName(p.getDisplayName() + ChatColor.translateAlternateColorCodes('&', tabSuffix));
	  }
	  
	  public static void setChatName(Player p, String name){
	    name = ChatColor.translateAlternateColorCodes('&', name);
	    
	    p.setDisplayName(name);
	  }
	  
	  public static void setTabName(Player p, String name){
	    name = ChatColor.translateAlternateColorCodes('&', name);
	    
	    p.setPlayerListName(name);
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void setName(Player p, String name){
	    name = ChatColor.translateAlternateColorCodes('&', name);
	    try{
	      Method getHandle = p.getClass().getMethod("getHandle", new Class[0]);
	      Object profile = getHandle.invoke(p, new Object[0]).getClass().getMethod("getProfile", new Class[0]).invoke(getHandle.invoke(p, new Object[0]), new Object[0]);
	      Field f = profile.getClass().getDeclaredField("name");
	      f.setAccessible(true);
	      f.set(profile, name);
	    }
	    catch (NoSuchFieldException e){
	      e.printStackTrace();
	    }
	    catch (SecurityException e){
	      e.printStackTrace();
	    }
	    catch (IllegalArgumentException e){
	      e.printStackTrace();
	    }
	    catch (IllegalAccessException e){
	      e.printStackTrace();
	    }
	    catch (NoSuchMethodException e){
	      e.printStackTrace();
	    }
	    catch (InvocationTargetException e) {
	      e.printStackTrace();
	    }
	    MinecraftServer nmsServer = ((CraftServer)Bukkit.getServer()).getServer();
	    WorldServer nmsWelt = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
	    
	    EntityPlayer ep = ((CraftPlayer)p).getHandle();
	    EntityPlayer pNeu = new EntityPlayer(nmsServer, nmsWelt, new GameProfile(p.getUniqueId(), name), new PlayerInteractManager(nmsWelt));
	    for (Player all : Bukkit.getOnlinePlayers()){
	      ((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep }));
	      ((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { pNeu }));
	      if (all != p) {
	        ((CraftPlayer)all).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(ep));
	      }
	    }
	    p.setDisplayName(name);
	    for (Player all : Bukkit.getOnlinePlayers()){
	      all.hidePlayer(p);
	      all.showPlayer(p);
	    }
	    setSkin(p, getNickName(p));
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void setSkin(Player p, String name){
	    GameProfile gp = ((CraftPlayer)p).getProfile();
	    gp.getProperties().clear();
	    GameProfile skinGF = null;
		try {skinGF = GameProfileBuilder.fetch(uuidfetcher.getUUID(name));}catch(Exception e) {e.printStackTrace();}
		Collection<Property> props = skinGF.getProperties().get("textures");
		gp.getProperties().putAll("textures", props);		
	    for (Player all : Bukkit.getOnlinePlayers()){
	      all.hidePlayer(p);
	      all.showPlayer(p);
	    }
	  }
	  
      @SuppressWarnings("deprecation")
      public String getUUID(String name){
          return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replace("-", "");
      }
	  
	  public static boolean isNicked(Player p){
	    if (Utils.nicked.containsKey(p.getUniqueId())){
	      return true;
	    }
	    return false;
	  }
}
