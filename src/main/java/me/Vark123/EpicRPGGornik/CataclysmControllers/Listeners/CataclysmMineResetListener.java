package me.Vark123.EpicRPGGornik.CataclysmControllers.Listeners;

import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.nikl.calendarevents.CalendarEvent;

public class CataclysmMineResetListener implements Listener {
	
	@EventHandler
	public void onDate(CalendarEvent e) {
		if(!e.getLabels().contains("reset_mines")) 
			return;
		
		PlayerManager.get().getMiners().values().forEach(miner -> {
			miner.getCatMiner().setRemainTime(miner.getCatMiner().getMaxTime());
			miner.getCatMiner().setLastResetTime(new Date().getTime());
		});
	}

}
