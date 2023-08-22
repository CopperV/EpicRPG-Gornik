package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners.Cataclysm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.OreSystem.AOre;
import me.Vark123.EpicRPGGornik.OreSystem.OreManager;
import me.Vark123.EpicRPGGornik.OreSystem.Cataclysm.CatOre;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;

public class CataclysmOreGatheringListener implements Listener {

	private static Map<Player, Date> playerCd = new HashMap<>();
	
	@EventHandler
	public void onClick(PlayerAnimationEvent e) {
		Player p = e.getPlayer();
		if(!p.getGameMode().equals(GameMode.ADVENTURE))
			return;
		
		if(playerCd.containsKey(p) && playerCd.get(p).getTime() + 250 > new Date().getTime())
			return;
		playerCd.put(p, new Date());
		
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
		
		if(!miner.getCatMiner().isJoinDelay()) {
			p.sendMessage("§7["+Main.getInst().getPrefix()+"§7] §cDopiero co wbiles na serwer. Poczekaj jeszcze troche!");
			return;
		}
		
		if(miner.getMiningOre() != null && miner.getMiningOre().equals(ore)) 
			return;
		
		if(miner.getCatMiner().getMinedOres().contains(ore))
			return;
		
		ItemStack pickaxe = p.getInventory().getItemInMainHand();
		if(pickaxe == null 
				|| !pickaxe.getType().toString().contains("PICKAXE"))
			return;
		
		NBTItem nbt = new NBTItem(pickaxe);
		if(!nbt.hasTag("mine_power"))
			return;
		
		double power = Double.valueOf(nbt.getString("mine_power"));
		miner.getCatMiner().startMiningOre((CatOre) ore, power);
		
	}
	
}
