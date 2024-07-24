package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGGornik.Main;

@Getter
public class TanalorrUpgradesMenuManager {

	private static final TanalorrUpgradesMenuManager inst = new TanalorrUpgradesMenuManager();

	private final ItemStack empty;
	private final ItemStack upgrade;
	
	private final InventoryProvider baseProvider;
	
	private TanalorrUpgradesMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		upgrade = new ItemStack(Material.NETHERITE_PICKAXE);{
			ItemMeta im = upgrade.getItemMeta();
			im.setDisplayName("§6ULEPSZ");
			upgrade.setItemMeta(im);
		}
		
		baseProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 4)
						continue;
					contents.set(i, empty);
				}
				
				contents.set(8, IntelligentItem.of(upgrade, e -> {
					ItemStack it = e.getClickedInventory().getItem(4);
					if(it == null || it.getType().equals(Material.AIR)) {
						player.closeInventory();
						return;
					}
					
					NBTItem nbt = new NBTItem(it);
					if(!nbt.hasTag("type") 
							|| !nbt.getString("type").equalsIgnoreCase("pickaxe-tanalorr")) {
						player.closeInventory();
						return;
					}
					
					e.getClickedInventory().setItem(4, null);
					openMenu(player, new MutableObject<>(it));
				}));
			}
			@Override
			public void close(Player player, RyseInventory inventory) {
				ItemStack it = inventory.getInventory().getItem(4);
				if(it == null || it.getType().equals(Material.AIR))
					return;
				
				Utils.dropItemStack(player, it);
			}
		};
	}
	
	public static final TanalorrUpgradesMenuManager get() {
		return inst;
	}
	
	public void openBaseMenu(Player p) {
		RyseInventory.builder()
			.title("§6§lULEPSZ KILOF")
			.rows(1)
			.disableUpdateTask()
			.ignoredSlots(4)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(baseProvider)
			.build(Main.getInst())
			.open(p);
	}
	
	private void openMenu(Player p, MutableObject<ItemStack> pickaxe) {
		RyseInventory.builder()
			.title("§6§lULEPSZ KILOF")
			.rows(1)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 9; ++i)
						contents.set(i, empty);

					ItemStack _it = pickaxe.getValue();
					NBTItem nbt = new NBTItem(_it);
					int lucky = nbt.getInteger("lucky");
					int handle = nbt.getInteger("handle");
					int edge = nbt.getInteger("edge");
					int convenience = nbt.getInteger("convenience");
					int prospector = nbt.getInteger("prospector");
					
					ATanalorrUpgrade luckyUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.LUCKY, lucky + 1);
					ATanalorrUpgrade handleUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.HANDLE, handle + 1);
					ATanalorrUpgrade edgeUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.EDGE, edge + 1);
					ATanalorrUpgrade convenienceUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.CONVENIENCE, convenience + 1);
					ATanalorrUpgrade prospectorUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.PROSPECTOR, prospector + 1);
					
					if(luckyUpgrade != null){
						ItemStack it = new ItemStack(Material.GOLD_NUGGET);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(luckyUpgrade.getDisplay()+luckyUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						luckyUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						
						contents.set(0, IntelligentItem.of(it, e -> {
							ItemStack newPickaxe = luckyUpgrade.upgrade(p, pickaxe.getValue());
							pickaxe.setValue(newPickaxe);
							p.closeInventory();
						}));
					}
					if(handleUpgrade != null){
						ItemStack it = new ItemStack(Material.STICK);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(handleUpgrade.getDisplay()+handleUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						handleUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						
						contents.set(2, IntelligentItem.of(it, e -> {
							ItemStack newPickaxe = handleUpgrade.upgrade(p, pickaxe.getValue());
							pickaxe.setValue(newPickaxe);
							p.closeInventory();
						}));
					}
					if(edgeUpgrade != null){
						ItemStack it = new ItemStack(Material.IRON_INGOT);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(edgeUpgrade.getDisplay()+edgeUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						edgeUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						
						contents.set(4, IntelligentItem.of(it, e -> {
							ItemStack newPickaxe = edgeUpgrade.upgrade(p, pickaxe.getValue());
							pickaxe.setValue(newPickaxe);
							p.closeInventory();
						}));
					}
					if(convenienceUpgrade != null){
						ItemStack it = new ItemStack(Material.REDSTONE);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(convenienceUpgrade.getDisplay()+convenienceUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						convenienceUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						
						contents.set(6, IntelligentItem.of(it, e -> {
							ItemStack newPickaxe = convenienceUpgrade.upgrade(p, pickaxe.getValue());
							pickaxe.setValue(newPickaxe);
							p.closeInventory();
						}));
					}
					if(prospectorUpgrade != null){
						ItemStack it = new ItemStack(Material.GOLD_ORE);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(prospectorUpgrade.getDisplay()+prospectorUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						prospectorUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						
						contents.set(8, IntelligentItem.of(it, e -> {
							ItemStack newPickaxe = prospectorUpgrade.upgrade(p, pickaxe.getValue());
							pickaxe.setValue(newPickaxe);
							p.closeInventory();
						}));
					}
				}
				@Override
				public void close(Player player, RyseInventory inventory) {
					if(pickaxe.getValue() == null)
						return;
					
					Utils.dropItemStack(player, pickaxe.getValue());
				}
			})
			.build(Main.getInst())
			.open(p);
	}
	
}
