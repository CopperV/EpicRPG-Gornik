package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners;

import java.util.Optional;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;

public class PlayerArmorChangeListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		if(e.getClickedInventory() == null)
			return;
		if(!e.getInventory().getType().equals(InventoryType.CRAFTING))
			return;
		
		Optional<PlayerMiner> oMiner = PlayerManager.get()
				.getPlayer((Player) e.getWhoClicked());
		if(oMiner.isEmpty())
			return;
		PlayerMiner miner = oMiner.get();
		if(e.getSlotType().equals(SlotType.ARMOR)) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		
		if(!(e.getClick().equals(ClickType.SHIFT_LEFT) 
				|| e.getClick().equals(ClickType.SHIFT_RIGHT)))
			return;

		
		String name = e.getCurrentItem().getType().name();
		PlayerInventory inv = e.getWhoClicked().getInventory();
		if(name.contains("BOOTS") && inv.getBoots() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("LEGGINGS") && inv.getLeggings() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("CHESTPLATE") && inv.getChestplate() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("HELMET") && inv.getHelmet() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Optional<PlayerMiner> oMiner = PlayerManager.get().getPlayer(p);
		if(oMiner.isEmpty())
			return;
		PlayerMiner miner = oMiner.get();
		PlayerInventory inv = p.getInventory();

		if(inv.getItemInMainHand() == null
				&& inv.getItemInOffHand() == null) 
			return;
		
		String name = inv.getItemInMainHand()!=null ? inv.getItemInMainHand().getType().name() : inv.getItemInOffHand().getType().name();
		if(name.contains("BOOTS") && inv.getBoots() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("LEGGINGS") && inv.getLeggings() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("CHESTPLATE") && inv.getChestplate() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
		if(name.contains("HELMET") && inv.getHelmet() == null) {
			miner.getCatMiner().calculateDebuffs();
			return;
		}
	}
	
	@EventHandler
	public void onDrop(InventoryDragEvent e) {
		if(e.getOldCursor() == null)
			return;
		
		Optional<PlayerMiner> oMiner = PlayerManager.get()
				.getPlayer((Player) e.getWhoClicked());
		if(oMiner.isEmpty())
			return;
		PlayerMiner miner = oMiner.get();
		
		String name = e.getOldCursor().getType().name();
		if(!(name.contains("BOOTS") || 
				name.contains("LEGGINGS") ||
				name.contains("CHESTPLATE") ||
				name.contains("HELMENT")))
			return;
		Set<Integer> slot = e.getRawSlots();
		if(!(slot.contains(5) || slot.contains(6) || slot.contains(7) || slot.contains(8)))
			return;
		miner.getCatMiner().calculateDebuffs();
	}
	
}
