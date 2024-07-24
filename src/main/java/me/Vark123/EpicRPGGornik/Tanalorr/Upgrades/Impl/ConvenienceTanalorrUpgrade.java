package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.ATanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;

public class ConvenienceTanalorrUpgrade extends ATanalorrUpgrade {

	private double health;
	private double speed;
	
	public ConvenienceTanalorrUpgrade(String id, int level, TanalorrUpgradeType type, String display, String valueDisplay,
			double value, Collection<ICost> costs, double health, double speed) {
		super(id, level, type, display, valueDisplay, value, costs);
		this.health = health;
		this.speed = speed;
	}

	@Override
	public ItemStack upgrade(Player p, ItemStack pickaxe) {
		ItemStack it = super.upgrade(p, pickaxe);
		if(it.equals(pickaxe))
			return it;
		
		NBTItem nbt = new NBTItem(it);
		
		NBTCompoundList stats = nbt.getCompoundList("AttributeModifiers");
		for(int i = 0; i < stats.size(); ++i) {
			NBTListCompound lc = stats.get(i);
			switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
				case "max_health":
					lc.setDouble("Amount", health);
					break;
				case "movement_speed":
					lc.setDouble("Amount", speed);
					break;
			}
		}
		
		nbt.applyNBT(it);
		
		return it;
	}
	
	

}
