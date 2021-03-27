package de.ilovejava.crafttitle;

import de.ilovejava.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class API_ActionBar {
    
	final static HashMap<String, Integer> COUNT = new HashMap<>();
	
	public static void sendActionBarTime(final Player PLAYER, final String MESSAGE,final Integer ZEIT) {
           final String NachrichtNeu = MESSAGE.replace("_", " ");
           
            if (!COUNT.containsKey(PLAYER.getName())) {
                    String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
                      IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s +
                        "\"}");
                      PacketPlayOutChat bar = new PacketPlayOutChat(icbc,ChatMessageType.GAME_INFO, PLAYER.getUniqueId());
                      ((CraftPlayer)PLAYER).getHandle().playerConnection.sendPacket(bar);
            }
           
            Bukkit.getScheduler().runTaskLater(Utils.getInstance(), () -> {
                     String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
                        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s +
                          "\"}");
                        PacketPlayOutChat bar = new PacketPlayOutChat(icbc,ChatMessageType.GAME_INFO, PLAYER.getUniqueId());
                        ((CraftPlayer)PLAYER).getHandle().playerConnection.sendPacket(bar);

                        if (!COUNT.containsKey(PLAYER.getName())){
                            COUNT.put(PLAYER.getName(),0);
                        }
                        int count = COUNT.get(PLAYER.getName());
                        int newCount = count+20;
                        COUNT.put(PLAYER.getName(), newCount);

                        if (newCount < ZEIT-20){
                            wait(PLAYER,MESSAGE,ZEIT);
                        } else{
                            COUNT.remove(PLAYER.getName());
                        }
            }, 10);
    }

    private static void wait(final Player PLAYER, final String MESSAGE,final Integer ZEIT){
            Bukkit.getScheduler().runTaskLater(Utils.getInstance(), () -> sendActionBarTime(PLAYER,MESSAGE,ZEIT), 10);
    }
}