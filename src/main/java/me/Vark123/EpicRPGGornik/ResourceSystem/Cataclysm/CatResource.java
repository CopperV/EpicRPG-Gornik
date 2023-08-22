package me.Vark123.EpicRPGGornik.ResourceSystem.Cataclysm;

import lombok.Getter;
import me.Vark123.EpicRPGGornik.ResourceSystem.AResource;

@Getter
public class CatResource extends AResource {

	private int coinPrice;
	
	public CatResource(String mmId, double chance, int coinPrice) {
		super(mmId, chance);
		
		this.coinPrice = coinPrice;
	}

}
