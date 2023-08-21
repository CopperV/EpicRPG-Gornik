package me.Vark123.EpicRPGGornik.PlayerSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.mojang.datafixers.util.Pair;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.OreSystem.Cataclysm.CatOre;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.world.entity.EnumItemSlot;

@Getter
public class CataclysmMiner {

	Player player;
	@Setter
	PlayerMiner miner;
	//TLEN
	@Setter
	private int maxTime;
	@Setter
	private int remainTime;
	
	private BukkitTask joinDelayTask;
	private boolean joinDelay;
	
	private double oreDurability;
	private BukkitTask miningTask;
	
	private Collection<CatOre> minedOres = new HashSet<>();
	private Collection<BukkitTask> minedOresTasks = new HashSet<>();
	
	private BossBar progressBar;
	
	//DEBUFFS
	@Setter
	private double debuffGathering;
	
	public CataclysmMiner(int maxTime, int remainTime, Player player) {
		super();
		this.maxTime = maxTime;
		this.remainTime = remainTime;
		this.player = player;
		
		progressBar = Bukkit.createBossBar(null, BarColor.RED, BarStyle.SOLID);
		progressBar.setVisible(false);
		progressBar.addPlayer(player);
		
		joinDelayTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				joinDelay = true;
			}
		}.runTaskLater(Main.getInst(), 20*60);
	}
	
	public void endTasks() {
		if(joinDelayTask != null)
			joinDelayTask.cancel();
		if(miningTask != null)
			miningTask.cancel();
		minedOresTasks.forEach(task -> task.cancel());
	}
	
	//TODO Add bossbar
	public void startMiningOre(CatOre ore, double power) {
		miner.setMiningOre(ore);
		
		if(miningTask != null && !miningTask.isCancelled())
			return;
		
		progressBar.setProgress(1);
		progressBar.setVisible(true);
		progressBar.setTitle("§c§lSpaczona ruda §f- §e§o100%");
		
		this.oreDurability = ore.getDurability();
		Location loc = ore.getBlockLocation().clone().add(0.5,-0.8,0.5);
		Location oreLoc = ore.getBlockLocation().clone().add(0.5,0.5,0.5);
		miningTask = new BukkitRunnable() {
			BlockData stone = Bukkit.createBlockData(Material.STONE);
			BlockData red = Bukkit.createBlockData(Material.RED_TERRACOTTA);
			PacketPlayOutAnimation swing = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 0);
			@Override
			public void run() {
				if(isCancelled()) {
					miner.setMiningOre(null);
					progressBar.setVisible(false);
					return;
				}
				if(!oreLoc.getWorld().getName().equals(player.getWorld().getName())) {
					miner.setMiningOre(null);
					progressBar.setVisible(false);
					cancel();
					return;
				}
				if(oreLoc.distance(player.getLocation()) > 6.5) {
					miner.setMiningOre(null);
					progressBar.setVisible(false);
					cancel();
					return;
				}
				if(!isOreInFOV(ore, 60)) {
					miner.setMiningOre(null);
					progressBar.setVisible(false);
					cancel();
					return;
				}
				
				oreDurability -= power * (1-debuffGathering);

				((CraftPlayer)player).getHandle().b.a(swing);
				player.playSound(loc, Sound.ENTITY_FISHING_BOBBER_THROW,1,.1f);
				loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.7f,0.7f,0.7f,0.15f,stone);
				loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.7f,0.7f,0.7f,0.15f,red);
				
				updateProgressBar(ore);

				if(oreDurability <= 0) {
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					player.playSound(ore.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
					loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.add(0, 0.3, 0), 100, 0.7f,0.7f,0.7f,0.15f,red);

					miner.setMiningOre(null);
					progressBar.setVisible(false);
					completeMining(ore);
					cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInst(), 0, 20*1);
	}
	
	public void completeMining(CatOre ore) {
		minedOres.add(ore);

		//TODO
		//DODANIE DROPU
		player.sendMessage("Drop itemu");
		
		Map<ArmorStand,List<Pair<EnumItemSlot,net.minecraft.world.item.ItemStack>>> eqs = new LinkedHashMap<>();
		ore.getOre().forEach(stand -> {
			List<Pair<EnumItemSlot,net.minecraft.world.item.ItemStack>> eq = new LinkedList<>();
			Pair<EnumItemSlot,net.minecraft.world.item.ItemStack> pair = new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(stand.getEquipment().getHelmet()));
			eq.add(pair);
			eqs.put(stand, eq);
			
			List<Pair<EnumItemSlot,net.minecraft.world.item.ItemStack>> newEq = new ArrayList<>();
			Pair<EnumItemSlot,net.minecraft.world.item.ItemStack> newPair = new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
			newEq.add(newPair);
			
			PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(stand.getEntityId(), newEq);
			((CraftPlayer)player).getHandle().b.a(packet);
		});
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				minedOres.remove(ore);
				eqs.forEach((stand, eq) -> {
					PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(stand.getEntityId(), eq);
					((CraftPlayer)player).getHandle().b.a(packet);
				});
			}
		}.runTaskLater(Main.getInst(), 20*60);
		minedOresTasks.add(task);
	}
	
	private boolean isOreInFOV(CatOre ore, double angle) {
		Player p = player;
		Location oreLoc = ore.getBlockLocation().clone().add(0.5,0.5,0.5);
		
		Vector playerVector = p.getEyeLocation().toVector();
		Vector oreVector = oreLoc.toVector();
		Vector headDir = p.getEyeLocation().getDirection();
		Vector targetDir = oreVector.subtract(playerVector).normalize();
		
		double targetAngle = Math.toDegrees((double)targetDir.angle(headDir));
		return targetAngle < angle;
	}
	
	private void updateProgressBar(CatOre ore) {
		double percent = oreDurability / ore.getDurability();
		if(percent < 0)
			percent = 0;
		if(percent > 1)
			percent = 1;
		progressBar.setProgress(percent);
		progressBar.setTitle("§c§lSpaczona ruda §f- §e§o"+String.format("%.2f", percent * 100)+"%");
	}
	
}