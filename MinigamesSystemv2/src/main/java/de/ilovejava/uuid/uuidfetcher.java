package de.ilovejava.uuid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ilovejava.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class uuidfetcher{
	
	   public static final long FEBRUARY_2015 = 1422748800000L;
	   
	   
	   private static Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new Typeadapter()).create();
	   
	   private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
	   private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
	   private static Map<String, UUID> uuidCache = new HashMap<String, UUID>();
	   private static Map<UUID, String> nameCache = new HashMap<UUID, String>();
	   private static ExecutorService pool = Executors.newCachedThreadPool();
	   
	   
	   private String name;
	   private UUID id;

	   
	   public static UUID getUUID(String name) {
		   return getUUIDAt(name, System.currentTimeMillis());
	   }
	   
	   public static UUID getUUID(Player p) {
		   EntityPlayer ep = ((CraftPlayer)p).getHandle();
		   if(Utils.getProfielUUIDS().containsKey(ep.getProfile())) {
			   return Utils.getProfielUUIDS().get(ep.getProfile());
		   }
		   return getUUIDAt(p.getName(), System.currentTimeMillis());
	   }
	  
	   public static void getUUIDAt(String name, long timestamp, Consumer<UUID> action) {
	      pool.execute(() -> action.accept(getUUIDAt(name, timestamp)));
	   }
	   
	   public static UUID getUUIDAt(String name, long timestamp) {
	      name = name.toLowerCase();
	      if (uuidCache.containsKey(name)) {
	         return uuidCache.get(name);
	      }
	      try {
	         HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp/1000)).openConnection();
	         connection.setReadTimeout(5000);
	         uuidfetcher data = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), uuidfetcher.class);
	         
	         uuidCache.put(name, data.id);
	         nameCache.put(data.id, data.name);
	         return data.id;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      
	      return null;
	   }
	   
	   public static void getName(UUID uuid, Consumer<String> action) {
	      pool.execute(() -> action.accept(getName(uuid)));
	   }
	
	   public static String getName(UUID uuid) {
	      if (nameCache.containsKey(uuid)) {
	         return nameCache.get(uuid);
	      }
	      try {
	         HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, Typeadapter.fromUUID(uuid))).openConnection();
	         connection.setReadTimeout(5000);
	         uuidfetcher[] nameHistory = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), uuidfetcher[].class);
	         uuidfetcher currentNameData = nameHistory[nameHistory.length - 1];
	         uuidCache.put(currentNameData.name.toLowerCase(), uuid);
	         nameCache.put(uuid, currentNameData.name);
	         return currentNameData.name;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return null;
	   }
	
}
