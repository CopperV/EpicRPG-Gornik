package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;

@Getter
@AllArgsConstructor
public abstract class ATanalorrUpgrade {

	protected String id;
	protected int level;
	protected TanalorrUpgradeType type;
	
	protected String display;
	protected String valueDisplay;
	protected double value;
	
	protected Collection<ICost> costs;
	
	public ItemStack upgrade(Player p, ItemStack pickaxe) {
		MutableBoolean canUpgrade = new MutableBoolean(true);
		costs.stream()
			.filter(cost -> !cost.check(p))
			.findAny()
			.ifPresent(cost -> canUpgrade.setFalse());
		
		if(canUpgrade.isFalse()) {
			p.sendMessage("§7"+Main.getInst().getPrefix()+"§7 §c§oMasz za malo surowcow na to ulepszenie!");
			return pickaxe;
		}
		
		costs.forEach(cost -> cost.spend(p));
		
		ItemStack it = pickaxe.clone();
		
		ItemMeta im = it.getItemMeta();
		List<String> lore = im.getLore();
		int slot = lore.stream()
				.filter(line -> line.contains(display))
				.map(lore::indexOf)
				.findAny()
				.orElseGet(() -> -1);
		if(slot >= 0)
			lore.set(slot, display+valueDisplay);
		im.setLore(lore);
		it.setItemMeta(im);
		
		NBTItem nbt = new NBTItem(it);
		nbt.setInteger(type.name().toLowerCase(), level);
		nbt.applyNBT(it);
		
		p.playSound(p, Sound.BLOCK_ANVIL_USE, 1, 0.85f);
		p.spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation().clone().add(0, 1.25, 0), 24, .6f, .6f, .6f, .15f);
		
		return it;
	}
	
}
