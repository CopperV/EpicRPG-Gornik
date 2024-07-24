package me.Vark123.EpicRPGGornik.Tanalorr.Listeners;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPGGornik.Tanalorr.TanalorrConfig;
import me.Vark123.EpicRPGGornik.Tanalorr.TanalorrController;

public class TanalorrBlockBreakListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		GameMode gm = p.getGameMode();
		if(!(gm.equals(GameMode.ADVENTURE) || gm.equals(GameMode.SURVIVAL)))
			return;
		
		String world = p.getWorld().getName();
		if(!TanalorrConfig.get().getAllowedWorlds().contains(world))
			return;
		
		if(!TanalorrConfig.get().getOres().containsKey(b.getType()))
			return;
		
		e.setCancelled(true);
		
		ItemStack it = p.getInventory().getItemInMainHand();
		if(!TanalorrController.get().canDig(p, b, it))
			return;

		TanalorrController.get().dig(p, b, it);
	}
	
}
