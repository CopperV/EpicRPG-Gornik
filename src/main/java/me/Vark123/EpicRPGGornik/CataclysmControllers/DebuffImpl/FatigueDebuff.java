package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class FatigueDebuff extends ADebuff {

	public FatigueDebuff() {
		super("fatigue", .15);
	}

	@Override
	public void doAction(Player p) {
		p.sendMessage("Wyznaczam debuffa - fatigue");
	}

}
