package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.Cataclysm;

import java.util.Optional;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPGGornik.OreSystem.AOre;
import me.Vark123.EpicRPGGornik.OreSystem.OreManager;
import me.Vark123.EpicRPGGornik.OreSystem.Cataclysm.CatOre;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;

public class CataclysmOreGatheringListener implements Listener {

	@EventHandler
	public void onClick(PlayerAnimationEvent e) {
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.ADVENTURE))
			return;
		
		Block b = p.getTargetBlock((Set<Material>)null, 5);
		Optional<AOre> oOre = OreManager.get().getOre(b.getLocation());
		if(oOre.isEmpty())
			return;
		AOre ore = oOre.get();
		if(!(ore instanceof CatOre))
			return;
		
		Optional<PlayerMiner> oMiner = PlayerManager.get().getPlayer(p);
		if(oMiner.isEmpty())
			return;
		PlayerMiner miner = oMiner.get();
		
		if(miner.getMiningOre() != null && miner.getMiningOre().equals(ore)) {
			p.sendMessage("Obecna ruda");
			return;
		}
		
		ItemStack pickaxe = p.getInventory().getItemInMainHand();
		if(pickaxe == null 
				|| !pickaxe.getType().toString().contains("PICKAXE"))
			return;
		
		NBTItem nbt = new NBTItem(pickaxe);
		if(!nbt.hasTag("mine_power"))
			return;
		
		double power = Double.valueOf(nbt.getString("mine_power"));
		//TODO
		//Kod na kopanie
		
		p.sendMessage("Nowa ruda");
		miner.setMiningOre(ore);
		
	}
	
}
