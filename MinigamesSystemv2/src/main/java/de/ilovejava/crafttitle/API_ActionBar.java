package de.ilovejava.crafttitle;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.ilovejava.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class API_ActionBar {
	final static HashMap<String, Integer> COUNT = new HashMap<String,Integer>();
	public static void sendActionBarTime(final Player PLAYER, final String MESSAGE,final Integer ZEIT)
    {
           final String NachrichtNeu = MESSAGE.replace("_", " ");
           
            if(!COUNT.containsKey(PLAYER.getName())){
                    String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
                      IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s +
                        "\"}");
                      PacketPlayOutChat bar = new PacketPlayOutChat(icbc,ChatMessageType.GAME_INFO);
                      ((CraftPlayer)PLAYER).getHandle().playerConnection.sendPacket(bar);
            }
           
            Bukkit.getScheduler().runTaskLater(Utils.getInstance(), new Runnable() {
                  @Override
                  public void run() {
                           String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
                              IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s +
                                "\"}");
                              PacketPlayOutChat bar = new PacketPlayOutChat(icbc,ChatMessageType.GAME_INFO);
                              ((CraftPlayer)PLAYER).getHandle().playerConnection.sendPacket(bar);
                             
                              if(!COUNT.containsKey(PLAYER.getName())){
                            	  COUNT.put(PLAYER.getName(),0);
                              }
                              int count = COUNT.get(PLAYER.getName());
                              int newCount = count+20;
                              COUNT.put(PLAYER.getName(), newCount);
                             
                              if(newCount < ZEIT-20){
                            	  API_ActionBar.wait(PLAYER,MESSAGE,ZEIT);
                              }else{
                            	  COUNT.remove(PLAYER.getName());
                              }
                  }
          }, 10);
    }

    private static void wait(final Player PLAYER, final String MESSAGE,final Integer ZEIT){
            Bukkit.getScheduler().runTaskLater(Utils.getInstance(), new Runnable() {
                          @Override
                          public void run() {
                                  sendActionBarTime(PLAYER,MESSAGE,ZEIT);
                          }
                  }, 10);
                         
    }
}