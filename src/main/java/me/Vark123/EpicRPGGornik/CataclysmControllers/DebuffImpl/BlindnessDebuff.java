package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class BlindnessDebuff extends ADebuff {

	public BlindnessDebuff() {
		super("blindness", .15);
	}

	@Override
	public void doAction(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, .1f);
		PotionEffect effect1 = new PotionEffect(PotionEffectType.BLINDNESS, 20*14, 1);
		PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 20*14, 1);
		p.addPotionEffect(effect1);
		p.addPotionEffect(effect2);
	}

}
