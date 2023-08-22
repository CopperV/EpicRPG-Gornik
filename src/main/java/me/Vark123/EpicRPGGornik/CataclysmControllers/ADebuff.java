package me.Vark123.EpicRPGGornik.CataclysmControllers;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class ADebuff {

	private String id;
	private double chance;
	
	public abstract void doAction(Player p);
	
}
