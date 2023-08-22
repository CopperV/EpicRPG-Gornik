package me.Vark123.EpicRPGGornik;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGGornik.CataclysmControllers.Listeners.CataclysmMineResetListener;
import me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.PlayerArmorChangeListener;
import me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.PlayerExitMineListener;
import me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.PlayerQuitListener;
import me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.Cataclysm.CataclysmOreGatheringListener;

public final class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerExitMineListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerArmorChangeListener(), inst);

		Bukkit.getPluginManager().registerEvents(new CataclysmOreGatheringListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CataclysmMineResetListener(), inst);
	}
	
}
