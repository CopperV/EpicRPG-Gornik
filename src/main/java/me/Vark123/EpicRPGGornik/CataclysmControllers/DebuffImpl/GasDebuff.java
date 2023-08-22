package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

public class GasDebuff extends ADebuff {

	public GasDebuff() {
		super("gas", .15);
	}

	@Override
	public void doAction(Player p) {
		PlayerManager.get().getPlayer(p).ifPresent(miner -> {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 0.5f);
			EntityDamageEvent event = new EntityDamageEvent(p, DamageCause.POISON,
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.45* (1 - miner.getCatMiner().getGasEffect()));
			Bukkit.getPluginManager().callEvent(event);
			ManualDamage.doDamage(p, event.getFinalDamage(), event);
		});
	}

}
