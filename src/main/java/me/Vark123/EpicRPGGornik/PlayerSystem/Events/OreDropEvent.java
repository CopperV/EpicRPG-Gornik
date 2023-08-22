package me.Vark123.EpicRPGGornik.PlayerSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.OreSystem.AOre;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;

@Getter
public class OreDropEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private PlayerMiner miner;
	@Setter
	private ItemStack itemOre;
	private AOre ore;

	public OreDropEvent(PlayerMiner miner, ItemStack itemOre, AOre ore) {
		super();
		this.miner = miner;
		this.itemOre = itemOre;
		this.ore = ore;
	}
	
	@Setter
	private boolean cancelled;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
