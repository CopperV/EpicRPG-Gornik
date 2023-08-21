package me.Vark123.EpicRPGGornik.ResourceSystem;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class AResource {

	protected String mmId;
	protected double chance;
	
}
