package me.Vark123.EpicRPGGornik;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGGornik.MerchantSystem.MerchantCommand;
import me.Vark123.EpicRPGGornik.Tanalorr.Commands.TanalorrRepairCommand;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeCommand;

public final class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("oremerchant").setExecutor(new MerchantCommand());
		Bukkit.getPluginCommand("tanalorrrepair").setExecutor(new TanalorrRepairCommand());
		Bukkit.getPluginCommand("pickaxeupgrade").setExecutor(new TanalorrUpgradeCommand());
	}
	
}
