package me.Vark123.EpicRPGGornik.PlayerSystem;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CataclysmModificatedDebuff {

	private ADebuff debuff;
	@Setter
	private double modifier;
	
	public double getChance() {
		double toReturn = debuff.getChance() - modifier;
		if(toReturn < 0)
			toReturn = 0;
		return toReturn;
	}
	
}
