package me.Vark123.EpicRPGGornik;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPGGornik.OreSystem.OreManager;
import me.Vark123.EpicRPGGornik.OreSystem.Cataclysm.CatOre;

public final class FileManager {

	private static File playerDir = new File(Main.getInst().getDataFolder(), "players");
	
	private static File cataclysmDir = new File(Main.getInst().getDataFolder(), "cataclysm");
	private static File cataclysmResources = new File(cataclysmDir, "resources.yml");
	private static File cataclysmOres = new File(cataclysmDir, "ores.yml");
	
	private FileManager() { }
	
	public static void init() {
		
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		if(!playerDir.exists())
			playerDir.mkdir();
		
		if(!cataclysmDir.exists())
			cataclysmDir.mkdir();
		
		if(!cataclysmResources.exists())
			try {
				cataclysmResources.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		if(!cataclysmOres.exists())
			try {
				cataclysmOres.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		loadOres();
		loadResources();
	}
	
	private static void loadOres() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(cataclysmOres);
		fYml.getKeys(false).forEach(key -> {
			Material material = Material.valueOf(fYml.getString(key+".material"));
			World world = Bukkit.getWorld(fYml.getString(key+".world"));
			double x = fYml.getDouble(key+".x");
			double y = fYml.getDouble(key+".y");
			double z = fYml.getDouble(key+".z");
			double durability = fYml.getDouble(key+".durability");
			int chance = fYml.getInt(key+".chance");
			Location location = new Location(world, x, y, z);
			
			CatOre ore = new CatOre(location, durability);
			OreManager.get().addOre(ore);
		});
	}
	
	private static void loadResources() {
		
	}
	
}
