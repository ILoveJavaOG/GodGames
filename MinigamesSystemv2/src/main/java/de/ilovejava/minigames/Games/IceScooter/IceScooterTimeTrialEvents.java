package de.ilovejava.minigames.Games.IceScooter;

import com.mojang.datafixers.util.Pair;
import de.ilovejava.minigames.Communication.Tracker;
import de.ilovejava.minigames.GameLogic.Events;
import de.ilovejava.minigames.MapTools.CheckPoint;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.HashMap;
import java.util.List;

/**
 * Class to represent events during the race
 */
public class IceScooterTimeTrialEvents implements IceScooterEvents, Events {

	/**
	 * Method to handle moving of boats
	 *
	 * @param event(VehicleMoveEvent): Move event
	 * @param state(HashMap): Map of boats and prev and next checkpoints
	 * @param checkPoints(List): List of checkpoints
	 */
	public void onMove(VehicleMoveEvent event, HashMap<Boat, Pair<CheckPoint, CheckPoint>> state, List<CheckPoint> checkPoints) {
		Boat boat = (Boat) event.getVehicle();
		CheckPoint next = state.get(boat).getSecond();
		//Determine if checkpoint is hit
		if (next.distanceSquared(boat.getLocation()) <= 0.81) {
			//Increase lap count if first checkpoint was reached
			if (next.getNumber() == 1) {
				Player driver = (Player) boat.getPassengers().get(0);
				IceScooterTimeTrial race = (IceScooterTimeTrial) Tracker.getGame(driver);
				race.newLap(driver);
			}
			//Get next checkpoints
			state.put(boat, new Pair<>(next, checkPoints.get(next.getNumber() % checkPoints.size())));
		}
	}
}
