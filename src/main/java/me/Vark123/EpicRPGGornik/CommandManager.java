package me.Vark123.EpicRPGGornik;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGGornik.MerchantSystem.MerchantCommand;

public final class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("oremerchant").setExecutor(new MerchantCommand());
	}
	
}
