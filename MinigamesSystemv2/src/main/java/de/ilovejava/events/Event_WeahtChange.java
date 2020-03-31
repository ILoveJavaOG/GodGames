package de.ilovejava.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Event_WeahtChange implements Listener {
	@EventHandler
	public void onChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
}
