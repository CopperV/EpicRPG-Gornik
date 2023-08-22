package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

public class NoDebuff extends ADebuff {

	public NoDebuff() {
		super("no", 1);
	}

	@Override
	public void doAction(Player p) {
		p.sendMessage("Wyznaczam debuffa - nothing");
	}

}
