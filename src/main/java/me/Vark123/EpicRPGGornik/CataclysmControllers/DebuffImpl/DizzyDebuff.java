package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class DizzyDebuff extends ADebuff {

	public DizzyDebuff() {
		super("dizzy", .15);
	}

	@Override
	public void doAction(Player p) {
		p.sendMessage("Wyznaczam debuffa - dizzy");
	}

}
