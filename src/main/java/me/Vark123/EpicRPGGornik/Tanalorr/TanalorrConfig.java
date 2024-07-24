package me.Vark123.EpicRPGGornik.Tanalorr;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPGGornik.FileManager;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.ATanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradesManager;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.Impl.DragonCoinsCost;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.Impl.MoneyCost;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.Impl.RudaCost;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.Impl.StygiaCost;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl.ConvenienceTanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl.EdgeTanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl.HandleTanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl.StandardTanalorrUpgrade;

@Getter
public final class TanalorrConfig {

	private static final TanalorrConfig conf = new TanalorrConfig();
	
	private boolean init;
	
	private final Collection<String> allowedWorlds;
	private final TreeMap<Double, String> oreDrops;
	private final Map<Material, Double> ores;
	
	private int maxPlayerLevel;
	private int oreUpdateAmount;
	
	private final Random rand = new Random();
	
	private TanalorrConfig() {
		allowedWorlds = new HashSet<>();
		oreDrops = new TreeMap<>();
		ores = new LinkedHashMap<>();
	}
	
	public static final TanalorrConfig get() {
		return conf;
	}
	
	public ItemStack getRandomDrop(double lucky, double variation) {
		double max = oreDrops.lastKey();
		if((variation + lucky) > max)
			variation = max - lucky;
		
		double chance = rand.nextDouble(variation) + lucky;
		if(chance > max)
			chance = max;
		
		String mmId = oreDrops.ceilingEntry(chance).getValue();
		return MythicBukkit.inst().getItemManager().getItemStack(mmId);
	}
	
	public double getOreLuck(Material m) {
		return ores.getOrDefault(m, -1.);
	}
	
	public void load() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(
				FileManager.getTanalorrConfig());
		
		this.allowedWorlds.addAll(fYml.getStringList("worlds"));
		this.maxPlayerLevel = fYml.getInt("max-player-level");
		this.oreUpdateAmount = fYml.getInt("ore-update-amount");
		
		MutableDouble value = new MutableDouble();
		ConfigurationSection drops = fYml.getConfigurationSection("drops");
		drops.getKeys(false).stream()
			.map(key -> drops.getConfigurationSection(key))
			.forEach(dropSection -> {
				String mmId = dropSection.getString("id");
				double chance = dropSection.getDouble("chance");
				
				oreDrops.put(value.addAndGet(chance), mmId);
			});

		ConfigurationSection oreSection = fYml.getConfigurationSection("ores");
		oreSection.getKeys(false).stream()
			.map(key -> oreSection.getConfigurationSection(key))
			.forEach(section -> {
				Material m = Material.valueOf(section.getString("material").toUpperCase());
				double lucky = section.getDouble("lucky");
				ores.put(m, lucky);
			});
		
		ConfigurationSection upgrades = fYml.getConfigurationSection("upgrades");
		upgrades.getKeys(false).forEach(key -> {
			ConfigurationSection upgrade = upgrades.getConfigurationSection(key);
			
			final String display = ChatColor.translateAlternateColorCodes('&', upgrade.getString("display"));
			final TanalorrUpgradeType type = TanalorrUpgradeType.valueOf(key.toUpperCase());
			
			ConfigurationSection levels = upgrade.getConfigurationSection("levels");
			levels.getKeys(false).stream()
			.filter(StringUtils::isNumeric)
			.map(Integer::parseInt)
			.forEach(level -> {
				ConfigurationSection section = levels.getConfigurationSection(""+level);
				
				final String id = section.getString("id");
				final String valueDisplay = ChatColor.translateAlternateColorCodes('&', section.getString("value-display"));
				double val = section.getDouble("value");

				Collection<ICost> costs;
				if(section.getConfigurationSection("costs") != null)
					costs = getCosts(section.getConfigurationSection("costs"));
				else
					costs = new LinkedList<>();
				
				ATanalorrUpgrade _upgrade;
				switch(type) {
					case CONVENIENCE:
						{
							double health = section.getDouble("attributes.health");
							double speed = section.getDouble("attributes.speed");
							_upgrade = new ConvenienceTanalorrUpgrade(id, level, type, display, valueDisplay, val, costs, health, speed);
						}
						break;
					case EDGE:
						{
							Material m = Material.valueOf(section.getString("material").toUpperCase());
							_upgrade = new EdgeTanalorrUpgrade(id, level, type, display, valueDisplay, val, costs, m);
						}
						break;
					case HANDLE:
						_upgrade = new HandleTanalorrUpgrade(id, level, type, display, valueDisplay, val, costs);
						break;
					default:
						_upgrade = new StandardTanalorrUpgrade(id, level, type, display, valueDisplay, val, costs);
						break;
				}
				
				TanalorrUpgradesManager.get().registerUpgrade(_upgrade);
			});
		});
	}
	
	private Collection<ICost> getCosts(ConfigurationSection section) {
		Collection<ICost> costs = new LinkedList<>();
		
		if(section.contains("money"))
			costs.add(new MoneyCost(section.getInt("money")));
		if(section.contains("stygia"))
			costs.add(new StygiaCost(section.getInt("stygia")));
		if(section.contains("ruda"))
			costs.add(new RudaCost(section.getInt("ruda")));
		if(section.contains("coins"))
			costs.add(new DragonCoinsCost(section.getInt("coins")));
		
		return costs;
	}
	
}
