package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TanalorrUpgradeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("pickaxeupgrade"))
			return false;
		if(!sender.hasPermission("epicgornik.admin"))
			return false;
		if(args.length == 0) {
			sender.sendMessage("§c§lPoprawne uzycie komendy");
			sender.sendMessage("§a§o/pickaxeupgrade <player>");
			return false;
		}
		
		Player p = Bukkit.getPlayerExact(args[0]);
		if(p == null) {
			sender.sendMessage("§7"+args[0]+" §c§ojest offline!");
			return false;
		}
		
		TanalorrUpgradesMenuManager.get().openBaseMenu(p);
		return true;
	}

}
