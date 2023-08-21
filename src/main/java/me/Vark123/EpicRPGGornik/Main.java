package me.Vark123.EpicRPGGornik;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;
	
	private String prefix;
	
	@Override
	public void onEnable() {
		inst = this;
		
		prefix = "§x§b§e§6§5§2§5§lE§x§c§7§7§e§4§2§lp§x§d§0§9§8§5§e§li§x§d§9§b§1§7§b§lc§x§e§2§c§a§9§7§lR§x§e§b§e§4§b§4§lP§x§f§4§f§d§d§0§lG §x§e§0§e§3§b§a§lG§x§c§c§c§8§a§3§lo§x§b§8§a§e§8§d§lr§x§a§4§9§4§7§7§ln§x§9§0§7§9§6§0§li§x§7§c§5§f§4§a§lk";
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		FileManager.init();
		
	}

	@Override
	public void onDisable() {
		
	}
	
}
