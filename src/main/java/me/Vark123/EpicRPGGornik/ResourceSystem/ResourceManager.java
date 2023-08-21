package me.Vark123.EpicRPGGornik.ResourceSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableDouble;

import lombok.Getter;
import me.Vark123.EpicRPGGornik.ResourceSystem.Cataclysm.CatResource;

@Getter
public final class ResourceManager {
	
	private static final ResourceManager inst = new ResourceManager();
	
	private Collection<AResource> resources;
	
	private ResourceManager() {
		resources = new HashSet<>();
	}
	
	public static final ResourceManager get() {
		return inst;
	}
	
	public void registerResource(AResource resource) {
		resources.add(resource);
	}
	
	public Collection<AResource> getCataclysmResources() {
		return resources.stream()
				.filter(resource -> resource instanceof CatResource)
				.collect(Collectors.toSet());
	}
	
	public Optional<AResource> getRandomCataclysmResource() {
		MutableDouble maxChance = new MutableDouble();
		TreeMap<Double, AResource> resourceMap = new TreeMap<>();
		getCataclysmResources().forEach(resource -> {
			resourceMap.put(maxChance.addAndGet(resource.getChance()), resource);
		});
		if(resourceMap == null || resourceMap.isEmpty())
			return Optional.empty();
		
		Random rand = new Random();
		double randNum = rand.nextDouble(maxChance.getValue());
		
		return Optional.ofNullable(resourceMap.ceilingEntry(randNum).getValue());
	}

}
