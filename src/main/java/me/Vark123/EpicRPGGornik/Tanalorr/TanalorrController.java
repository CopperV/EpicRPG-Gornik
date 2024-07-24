package me.Vark123.EpicRPGGornik.Tanalorr;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;
import me.Vark123.EpicRPGGornik.PlayerSystem.Events.OreDropEvent;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradesManager;

@Getter
public final class TanalorrController {

	private static final TanalorrController inst = new TanalorrController();
	
	private static final int DEFAULT_REPAIR_COST = 2000;
	
	private final Map<Material, Integer> oreLevels;
	
	private final Map<Player, Collection<Location>> diggedOres;
	private final Map<Player, Collection<Location>> megaOres;
	
	private final FireworkEffect fe1;
	private final FireworkEffect fe2;
	private final FireworkEffect fe3;
	private final FireworkEffect fe4;
	
	private TanalorrController() {
		oreLevels = new LinkedHashMap<>();
		diggedOres = new ConcurrentHashMap<>();
		megaOres = new ConcurrentHashMap<>();
		
		oreLevels.put(Material.WAXED_WEATHERED_COPPER, 0);
		oreLevels.put(Material.WAXED_OXIDIZED_COPPER, 2);
		oreLevels.put(Material.WAXED_OXIDIZED_CUT_COPPER, 4);

		fe1 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.BLUE, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe2 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.RED, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe3 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.GREEN, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe4 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.YELLOW, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
	}
	
	public static final TanalorrController get() {
		return inst;
	}
	
	public boolean canDig(Player p, Block b, ItemStack it) {
		Material m = b.getType();
		if(!oreLevels.containsKey(m))
			return false;
		
		int level = oreLevels.get(m);
		NBTItem nbt = new NBTItem(it);
		int pickaxeDigLevel = nbt.getInteger("edge");
		
		return level <= pickaxeDigLevel;
	}
	
	public void dig(Player p, Block b, ItemStack it) {
		double luck = TanalorrConfig.get().getOreLuck(b.getType());
		if(luck < 0)
			return;
		
		if(megaOres.containsKey(p) 
				&& megaOres.get(p).contains(b.getLocation())) {
			luck += 0.2;
			megaOres.get(p).remove(b.getLocation());
			megaOreDigEffect(p);
		}

		Optional<PlayerMiner> oMiner = PlayerManager.get().getPlayer(p);
		if(oMiner.isEmpty())
			return;
		
		PlayerMiner miner = oMiner.get();
		
		NBTItem nbt = new NBTItem(it);
		double pickaxeLuck = nbt.getInteger("lucky") / 100.;
		
		Location eyeLoc = p.getEyeLocation();
		Vector vec = eyeLoc.clone().getDirection().normalize().multiply(-0.75);
		ItemStack drop = TanalorrConfig.get().getRandomDrop(luck + pickaxeLuck, miner.getTanMiner().getVariancy());
		Location loc = b.getLocation().clone().add(.5, .5, .5).add(vec);
		loc.add(vec);
		
		OreDropEvent event = new OreDropEvent(miner, drop, null);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		drop = event.getItemOre();
		
		loc.getWorld().dropItem(loc, drop);
		
		int prospectorLevel = nbt.getInteger("prospector");
		double prospector = TanalorrUpgradesManager.get().getUpgradeByLevel(TanalorrUpgradeType.PROSPECTOR, prospectorLevel).getValue();
		Random rand = new Random();
		if(rand.nextDouble() < prospector)
			markAsMegaOre(p, b);
		else
			markAsDigged(p, b);
		damagePickaxe(p, it);
		
		miner.getTanMiner().update();
		
		return;
	}
	
	private void markAsMegaOre(Player p, Block b) {
		Material m = b.getType();
		Location loc = b.getLocation();
		
		if(!megaOres.containsKey(p))
			megaOres.put(p, new HashSet<>());
		
		megaOres.get(p).add(loc);

		p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
		p.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().add(0, 1, 0), 16, 0.6f, 0.6f, 0.6f, 0.15f);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendBlockChange(loc, Bukkit.createBlockData(Material.EMERALD_BLOCK));
			}
		}.runTaskLater(Main.getInst(), 1);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!p.isOnline())
					return;
				if(!megaOres.containsKey(p))
					return;
				if(!megaOres.get(p).contains(loc))
					return;

				megaOres.get(p).remove(loc);
				p.sendBlockChange(loc, Bukkit.createBlockData(m));
			}
		}.runTaskLater(Main.getInst(), 20*15);
		
		new BukkitRunnable() {
			int timer = 20*15 - 1;
			@Override
			public void run() {
				if(timer <= 0) {
					cancel();
					return;
				}

				--timer;
				if(!p.isOnline()) {
					cancel();
					return;
				}
				if(!megaOres.containsKey(p)) {
					cancel();
					return;
				}
				if(!megaOres.get(p).contains(loc)) {
					cancel();
					return;
				}

				p.sendBlockChange(loc, Bukkit.createBlockData(Material.EMERALD_BLOCK));
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 1);
	}
	
	private void markAsDigged(Player p, Block b) {
		Material m = b.getType();
		Location loc = b.getLocation();
		
		if(!diggedOres.containsKey(p))
			diggedOres.put(p, new HashSet<>());
		
		diggedOres.get(p).add(loc);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendBlockChange(loc, Bukkit.createBlockData(Material.BEDROCK));
			}
		}.runTaskLater(Main.getInst(), 1);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!p.isOnline())
					return;
				if(!diggedOres.containsKey(p))
					return;
				if(!diggedOres.get(p).contains(loc))
					return;

				diggedOres.get(p).remove(loc);
				p.sendBlockChange(loc, Bukkit.createBlockData(m));
			}
		}.runTaskLater(Main.getInst(), 20*60);
		
		new BukkitRunnable() {
			int timer = 20*60 - 1;
			@Override
			public void run() {
				if(timer <= 0) {
					cancel();
					return;
				}

				--timer;
				if(!p.isOnline()) {
					cancel();
					return;
				}
				if(!diggedOres.containsKey(p)) {
					cancel();
					return;
				}
				if(!diggedOres.get(p).contains(loc)) {
					cancel();
					return;
				}

				p.sendBlockChange(loc, Bukkit.createBlockData(Material.BEDROCK));
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 1);
	}
	
	private void damagePickaxe(Player p, ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		int durability = nbt.getInteger("present-durability") - 1;
		int maxDurability = nbt.getInteger("durability");
		nbt.setInteger("present-durability", durability);
		nbt.applyNBT(it);
		
		double percent = (double) durability / (double) maxDurability;
		int itemMaxDurability = it.getType().getMaxDurability();
		int itemDurability = itemMaxDurability - (int) (percent * itemMaxDurability);
		if(itemDurability >= itemMaxDurability && durability > 0)
			itemDurability = itemMaxDurability-1;
		if(itemDurability >= itemMaxDurability) {
			p.getInventory().setItemInMainHand(null);
			p.playSound(p, Sound.ENTITY_ITEM_BREAK, 1, 1);
			p.spawnParticle(Particle.ITEM_CRACK, p.getLocation().add(0,1,0), 16, .5f, .5f, .5f, .1f, it);
			return;
		}
		
		Damageable im = (Damageable) it.getItemMeta();
		im.setDamage(itemDurability);
		it.setItemMeta(im);
	}
	
	public double getRepairCost(ItemStack it) {
		if(it == null || it.getType().equals(Material.AIR))
			return 0;
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("type")
				|| !nbt.getString("type").equals("pickaxe-tanalorr"))
			return 0;
		
		int lucky = nbt.getInteger("lucky");
		int handle = nbt.getInteger("handle");
		int edge = nbt.getInteger("edge");
		int convenience = nbt.getInteger("convenience");
		int prospector = nbt.getInteger("prospector");
		int durability = nbt.getInteger("durability");
		int presentDurability = nbt.getInteger("present-durability");
		
		double modifier = (1. - (double) presentDurability / (double) durability) * (1. + edge * 0.5);
		
		double tmp = 0;
		tmp += modifier * ((double) (lucky) / 100.);
		tmp += modifier * ((double) (handle) / 100.);
		tmp += modifier * ((double) (convenience) / 100.);
		tmp += modifier * ((double) (prospector) / 100.);
		
		modifier += tmp;
		double price = ((int)(DEFAULT_REPAIR_COST * modifier * 100)) / 100.;
		
		return price;
	}
	
	public ItemStack repairPickaxe(Player p, ItemStack it, double repairCost) {
		if(it == null || it.getType().equals(Material.AIR))
			return it;
		
		RpgPlayer rpg = me.Vark123.EpicRPG.Players.PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		if(!vault.hasEnoughMoney(repairCost))
			return it;

		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("type")
				|| !nbt.getString("type").equals("pickaxe-tanalorr"))
			return it;

		int durability = nbt.getInteger("durability");
		nbt.setInteger("present-durability", durability);
		nbt.applyNBT(it);

		Damageable im = (Damageable) it.getItemMeta();
		im.setDamage(0);
		it.setItemMeta(im);
		
		vault.removeMoney(repairCost);
		rpg.getPlayer().sendMessage("§e§o-"+ String.format("%.2f", repairCost) +"$ §7[§e§o"+String.format("%.2f", vault.getMoney())+"$§7]");
		
		p.playSound(p, Sound.BLOCK_ANVIL_USE, 1, .8f);
		p.spawnParticle(Particle.TOTEM, p.getLocation().add(0,1,0), 16, .5f, .5f, .5f, .1f);
		
		return it;
	}
	
	private void megaOreDigEffect(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 2);
		
		Firework f1 = p.getWorld().spawn(p.getLocation().add(1, 1, 0), Firework.class);
		Firework f2 = p.getWorld().spawn(p.getLocation().add(-1, 1, 0), Firework.class);
		Firework f3 = p.getWorld().spawn(p.getLocation().add(1, -1, 0), Firework.class);
		Firework f4 = p.getWorld().spawn(p.getLocation().add(-1, -1, 0), Firework.class);
		FireworkMeta fm1 = f1.getFireworkMeta();
		FireworkMeta fm2 = f2.getFireworkMeta();
		FireworkMeta fm3 = f3.getFireworkMeta();
		FireworkMeta fm4 = f4.getFireworkMeta();
		fm1.addEffect(fe1);
		fm2.addEffect(fe2);
		fm3.addEffect(fe3);
		fm4.addEffect(fe4);
		f1.setFireworkMeta(fm1);
		f2.setFireworkMeta(fm2);
		f3.setFireworkMeta(fm3);
		f4.setFireworkMeta(fm4);
	}
	
}
