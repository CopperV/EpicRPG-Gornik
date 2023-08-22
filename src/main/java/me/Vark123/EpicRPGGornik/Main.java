package me.Vark123.EpicRPGGornik;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicRPGGornik.CataclysmControllers.CatMineController;
import me.Vark123.EpicRPGGornik.OreSystem.OreManager;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;
	
	private String prefix;
	
	private InventoryManager inventoryManager;
	private CalendarEventsApi calendar;
	
	@Override
	public void onEnable() {
		inst = this;
		
		prefix = "§x§b§e§6§5§2§5§lE§x§c§7§7§e§4§2§lp§x§d§0§9§8§5§e§li§x§d§9§b§1§7§b§lc§x§e§2§c§a§9§7§lR§x§e§b§e§4§b§4§lP§x§f§4§f§d§d§0§lG §x§e§0§e§3§b§a§lG§x§c§c§c§8§a§3§lo§x§b§8§a§e§8§d§lr§x§a§4§9§4§7§7§ln§x§9§0§7§9§6§0§li§x§7§c§5§f§4§a§lk";

		CalendarEvents calend = (CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents");
		calendar = calend.getApi();
		inventoryManager = new InventoryManager(inst);
		inventoryManager.invoke();
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall all F_RPG_Cave");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall armorstand F_RPG_Cave");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall named F_RPG_Cave");
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		FileManager.init();
		
		CatMineController.get().start();
		
		if(calendar.isRegisteredEvent("reset_mines")) 
			calendar.removeEvent("reset_mines");
		calendar.addEvent("reset_mines", "every day", "00:15");
		
		Bukkit.getOnlinePlayers().forEach(PlayerManager.get()::registerPlayer);
	}

	@Override
	public void onDisable() {
		OreManager.get().clearOres();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall all F_RPG_Cave");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall armorstand F_RPG_Cave");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall named F_RPG_Cave");
		
		CatMineController.get().stop();

		Bukkit.getOnlinePlayers().forEach(PlayerManager.get()::unregisterPlayer);
	}
	
}
