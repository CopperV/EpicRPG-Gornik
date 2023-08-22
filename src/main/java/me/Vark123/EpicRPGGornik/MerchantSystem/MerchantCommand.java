package me.Vark123.EpicRPGGornik.MerchantSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPGGornik.Main;

public class MerchantCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("oremerchant"))
			return false;
		
		if(!sender.hasPermission("epicrpg-gornik.admin")) {
			sender.sendMessage("§7["+Main.getInst().getPrefix()+"§7] §cNie posiadasz uprawnien do tej komendy");
			return false;
		}
		
		if(args.length < 0) {
			sender.sendMessage("§7["+Main.getInst().getPrefix()+"§7] §ePoprawne uzycie komendy to §c§o/oremerchant <nick>");
			return false;
		}
		
		Player p = Bukkit.getPlayerExact(args[0]);
		if(p == null) {
			sender.sendMessage("§7["+Main.getInst().getPrefix()+"§7] §cGracz §e§o"+args[0]+" §cjest offline!");
			return false;
		}
		
		CataclysmMerchantManager.get().openMenu(p);
		return true;
	}

}
