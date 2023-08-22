package me.Vark123.EpicRPGGornik.MerchantSystem;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPGGornik.Main;

@Getter
public final class CataclysmMerchantManager {

	private static final CataclysmMerchantManager inst = new CataclysmMerchantManager();
	
	private final InventoryProvider provider;
	
	private final ItemStack empty;
	private final ItemStack sell;
	
	private final int[] freeSlots;
	
	private CataclysmMerchantManager() {
		freeSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		sell = new ItemStack(Material.BLAZE_ROD, 1);{
			ItemMeta im = sell.getItemMeta();
			im.setDisplayName("§c§lSprzedaj");
			sell.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 9; i < 18; ++i) {
					contents.set(i, empty);
				}
				contents.set(13, sell);
			}
		};
	}
	
	public static final CataclysmMerchantManager get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 1, .9f);
		RyseInventory.builder()
			.title("§c§lKupiec rud")
			.rows(2)
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(CataclysmMerchantEvents.getEvents().getClickEvent())
			.listener(CataclysmMerchantEvents.getEvents().getCloseEvent())
			.provider(provider)
			.build(Main.getInst())
			.open(p);
	}
	
}
