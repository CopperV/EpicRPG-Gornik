package me.Vark123.EpicRPGGornik.OreSystem.Cataclysm;

import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import lombok.Getter;
import me.Vark123.EpicRPGGornik.OreSystem.AOre;

@Getter
public class CatOre extends AOre {
	
	private ArmorStand display;
	private Collection<ArmorStand> ore;

	public CatOre(Location location, double durability) {
		super(Material.BARRIER, location, durability);
		
		ore = new LinkedList<>();
	}

	@Override
	public void createOre() {
		location.getBlock().setType(material);
		
		ItemStack item1 = new ItemStack(Material.STONE);
		ItemStack item2 = new ItemStack(Material.RED_TERRACOTTA);
		
		Location loc = location.clone().add(0.2,-0.35,0.2);
		
		Location tmpLoc = loc.clone();
		ArmorStand tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));
		tmp.getEquipment().setHelmet(item1);
		ore.add(tmp);
		
		tmpLoc = loc.clone().add(0.4,0,0);
		tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(155), Math.toRadians(20), 0));
		tmp.getEquipment().setHelmet(item2);
		ore.add(tmp);
		
		tmpLoc = loc.clone().add(0,-0.1,0.4);
		tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(165), Math.toRadians(335), 0));
		tmp.getEquipment().setHelmet(item2);
		ore.add(tmp);
		
		tmpLoc = loc.clone().add(0.4,-0.1,0.4);
		tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(210), Math.toRadians(355), 0));
		tmp.getEquipment().setHelmet(item2);
		ore.add(tmp);
		
		tmpLoc = loc.clone().add(0.6,-0.1,0.3);
		tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(180), Math.toRadians(345), Math.toRadians(10)));
		tmp.getEquipment().setHelmet(item1);
		ore.add(tmp);
		
		tmpLoc = loc.clone().add(0.3,-0.1,0.6);
		tmp = (ArmorStand) location.getWorld().spawnEntity(tmpLoc, EntityType.ARMOR_STAND);
		tmp.setHeadPose(new EulerAngle(Math.toRadians(170), Math.toRadians(45), 0));
		tmp.getEquipment().setHelmet(item1);
		ore.add(tmp);
		
		for(ArmorStand stand : ore) {
			stand.setArms(false);
			stand.setGravity(false);
			stand.setCanPickupItems(false);
			stand.setCollidable(false);
			stand.setInvulnerable(true);
			stand.setSmall(true);
			stand.setBasePlate(false);
			stand.setVisible(false);
		}
		
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			for(ArmorStand stand : ore) {
				stand.addEquipmentLock(slot, LockType.ADDING);
				stand.addEquipmentLock(slot, LockType.ADDING_OR_CHANGING);
				stand.addEquipmentLock(slot, LockType.REMOVING_OR_CHANGING);
			}
		}
		
		Location nameLoc = location.clone().add(0.5, -1, 0.5);
		display = (ArmorStand) nameLoc.getWorld().spawnEntity(nameLoc, EntityType.ARMOR_STAND);
		display.setGravity(false);
		display.setVisible(false);
		display.setCanPickupItems(false);
		display.setInvulnerable(true);
		display.setCustomNameVisible(true);
		display.setCustomName("§c§lSpaczona ruda");
		
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			display.addEquipmentLock(slot, LockType.ADDING);
			display.addEquipmentLock(slot, LockType.ADDING_OR_CHANGING);
			display.addEquipmentLock(slot, LockType.REMOVING_OR_CHANGING);
		}
	}

	@Override
	public void destroyOre() {
		display.remove();
		ore.forEach(as -> as.remove());
		ore.clear();
	}

	//TODO
	//When player system will be implemented
	@Override
	public void markAsMined(Player p) {
		
	}

}
