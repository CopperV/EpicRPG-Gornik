package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class GasDebuff extends ADebuff {

	public GasDebuff() {
		super("gas", .15);
	}

	@Override
	public void doAction(Player p) {
		p.sendMessage("Wyznaczam debuffa - gas");
	}

}
