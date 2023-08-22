package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class DizzyDebuff extends ADebuff {

	public DizzyDebuff() {
		super("dizzy", .15);
	}

	@Override
	public void doAction(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1, 1);
		Random rand = new Random();
		Location loc = p.getLocation();
		loc.setYaw(rand.nextFloat()*360);
		loc.setPitch(rand.nextFloat()*180-90);
		PotionEffect effect1 = new PotionEffect(PotionEffectType.BLINDNESS, 20*7, 1);
		PotionEffect effect2 = new PotionEffect(PotionEffectType.CONFUSION, 20*7, 1);
		p.addPotionEffect(effect1);
		p.addPotionEffect(effect2);
		p.teleport(loc);
	}

}
