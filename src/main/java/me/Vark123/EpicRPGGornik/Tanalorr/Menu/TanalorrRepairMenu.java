package me.Vark123.EpicRPGGornik.Tanalorr.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.enums.TimeSetting;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.Tanalorr.TanalorrController;

@Getter
public final class TanalorrRepairMenu {

	private static final TanalorrRepairMenu inst = new TanalorrRepairMenu();

	private final ItemStack empty;
	private final ItemStack info;
	private final ItemStack repair;
	
	private TanalorrRepairMenu() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		info = new ItemStack(Material.PAPER);{
			ItemMeta im = info.getItemMeta();
			im.setDisplayName("§6Koszt naprawy: §e§o");
			info.setItemMeta(im);
		}
		repair = new ItemStack(Material.ANVIL);{
			ItemMeta im = repair.getItemMeta();
			im.setDisplayName("§7§lNAPRAW");
			repair.setItemMeta(im);
		}
	}
	
	public static final TanalorrRepairMenu get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		RyseInventory.builder()
			.title("§7§lGORNICZY MAJSTER")
			.rows(2)
			.period(4, TimeSetting.MILLISECONDS)
			.ignoredSlots(4)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 18; ++i) {
						if(i == 4)
							continue;
						contents.set(i, empty);
					}
					contents.set(13, info);
					contents.set(8, IntelligentItem.of(repair, __ -> {
						Inventory inv = contents.pagination().inventory().getInventory();
						ItemStack it = inv.getItem(4);
						if(it != null && !it.getType().equals(Material.AIR)) {
							double price = TanalorrController.get().getRepairCost(it);
							it = TanalorrController.get().repairPickaxe(p, it, price);
							Utils.dropItemStack(p, it);
							contents.updateOrSet(4, new ItemStack(Material.AIR));
						}
						p.closeInventory();
					}));
				}
				@Override
				public void update(Player player, InventoryContents contents) {
					Inventory inv = contents.pagination().inventory().getInventory();
					ItemStack it = inv.getItem(4);
					if(it != null && !it.getType().equals(Material.AIR)) {
						double price = TanalorrController.get().getRepairCost(it);
						ItemStack _info = info.clone();
						
						ItemMeta im = _info.getItemMeta();
						im.setDisplayName(im.getDisplayName()+price+"$");
						_info.setItemMeta(im);
						
						contents.updateOrSet(13, _info);
					} else {
						contents.updateOrSet(13, info);
					}
				}
				@Override
				public void close(Player player, RyseInventory inventory) {
					ItemStack it = inventory.getInventory().getItem(4);
					if(it == null || it.getType().equals(Material.AIR))
						return;
					Utils.dropItemStack(player, it);
				}
			})
			.build(Main.getInst())
			.open(p);
	}
	
}
