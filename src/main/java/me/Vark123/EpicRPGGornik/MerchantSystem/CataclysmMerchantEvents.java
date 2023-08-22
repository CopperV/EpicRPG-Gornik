package me.Vark123.EpicRPGGornik.MerchantSystem;

import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGGornik.ResourceSystem.AResource;
import me.Vark123.EpicRPGGornik.ResourceSystem.ResourceManager;
import me.Vark123.EpicRPGGornik.ResourceSystem.Cataclysm.CatResource;

@Getter
public final class CataclysmMerchantEvents {

	private static final CataclysmMerchantEvents inst = new CataclysmMerchantEvents();

	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;

	private CataclysmMerchantEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
	}

	public static final CataclysmMerchantEvents getEvents() {
		return inst;
	}

	private final EventCreator<InventoryClickEvent> clickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			ItemStack it = e.getCurrentItem();
			if(it == null || !it.equals(CataclysmMerchantManager.get().getSell()))
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			
			double totalSale = 0;
			for(int slot : CataclysmMerchantManager.get().getFreeSlots()) {
				ItemStack drop = inv.getItem(slot);
				if(drop == null || drop.getType().equals(Material.AIR))
					continue;
				
				NBTItem nbt = new NBTItem(drop);
				if(!nbt.hasTag("MYTHIC_TYPE")) 
					continue;
				
				String mmId = nbt.getString("MYTHIC_TYPE");
				Optional<AResource> oResource = ResourceManager.get().getResourceByMMId(mmId);
				if(oResource.isEmpty())
					continue;
				AResource resource = oResource.get();
				if(!(resource instanceof CatResource))
					continue;
				
				int price = ((CatResource) resource).getCoinPrice();
				totalSale += price * drop.getAmount();
				
				inv.setItem(slot, null);
			}
			
			if(totalSale != 0) {
				CoinsSystem.getInstance().addCoins(rpg, (int) totalSale);
			}
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private final EventCreator<InventoryCloseEvent> closeEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Player p = (Player) e.getPlayer();
			Inventory inv = e.getView().getTopInventory();
			for(int slot : CataclysmMerchantManager.get().getFreeSlots()) {
				ItemStack it = inv.getItem(slot);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					continue;
				Utils.dropItemStack(p, it);
			}
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}

}
