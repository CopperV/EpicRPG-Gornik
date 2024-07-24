package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.ATanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;

public class HandleTanalorrUpgrade extends ATanalorrUpgrade {
	
	public HandleTanalorrUpgrade(String id, int level, TanalorrUpgradeType type, String display, String valueDisplay,
			double value, Collection<ICost> costs) {
		super(id, level, type, display, valueDisplay, value, costs);
	}

	@Override
	public ItemStack upgrade(Player p, ItemStack pickaxe) {
		ItemStack it = super.upgrade(p, pickaxe);
		if(it.equals(pickaxe))
			return it;
		
		NBTItem nbt = new NBTItem(it);
		int durability = (int) (nbt.getInteger("present-durability") + value);
		int maxDurability = (int) (value);
		
		nbt.setInteger("present-durability", durability);
		nbt.setInteger("durability", maxDurability);
		nbt.applyNBT(it);
		
		double percent = (double) durability / (double) maxDurability;
		int itemMaxDurability = it.getType().getMaxDurability();
		int itemDurability = itemMaxDurability - (int) (percent * itemMaxDurability);
		if(itemDurability >= itemMaxDurability && durability > 0)
			itemDurability = itemMaxDurability-1;
		
		Damageable im = (Damageable) it.getItemMeta();
		im.setDamage(itemDurability);
		it.setItemMeta(im);
		
		return it;
	}
	
	

}
