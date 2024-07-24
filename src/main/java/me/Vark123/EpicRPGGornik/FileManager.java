package me.Vark123.EpicRPGGornik;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPGGornik.OreSystem.OreManager;
import me.Vark123.EpicRPGGornik.OreSystem.Cataclysm.CatOre;
import me.Vark123.EpicRPGGornik.PlayerSystem.CataclysmMiner;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerMiner;
import me.Vark123.EpicRPGGornik.PlayerSystem.TanalorrMiner;
import me.Vark123.EpicRPGGornik.ResourceSystem.ResourceManager;
import me.Vark123.EpicRPGGornik.ResourceSystem.Cataclysm.CatResource;
import me.Vark123.EpicRPGGornik.Tanalorr.TanalorrConfig;

public final class FileManager {

	private static File playerDir = new File(Main.getInst().getDataFolder(), "players");
	
	private static File cataclysmDir = new File(Main.getInst().getDataFolder(), "cataclysm");
	private static File cataclysmResources = new File(cataclysmDir, "resources.yml");
	private static File cataclysmOres = new File(cataclysmDir, "ores.yml");

	private static File tanalorrDir = new File(Main.getInst().getDataFolder(), "tanalorr");
	@Getter
	private static File tanalorrConfig = new File(tanalorrDir, "tanalorr.yml");
	
	private FileManager() { }
	
	public static void init() {
		
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		if(!playerDir.exists())
			playerDir.mkdir();
		
		if(!cataclysmDir.exists())
			cataclysmDir.mkdir();
		
		if(tanalorrDir.exists())
			tanalorrDir.mkdir();
			
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
		
		Main.getInst().saveResource("tanalorr/tanalorr.yml", false);
		
		loadOres();
		loadResources();
		
		TanalorrConfig.get().load();
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
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(cataclysmResources);
		fYml.getKeys(false).forEach(key -> {
			String mythicItem = fYml.getString(key+".mythicItem");
			double chance = fYml.getDouble(key+".chance");
			int price = fYml.getInt(key+".price");
			
			CatResource resource = new CatResource(mythicItem, chance, price);
			ResourceManager.get().registerResource(resource);
		});
	}
	
	public static PlayerMiner loadPlayer(Player p) {
		File pFile = new File(playerDir, p.getUniqueId().toString()+".yml");
		if(!pFile.exists())
			try {
				pFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(pFile);
		int maxTime = fYml.getInt("cataclysm.max-time", 900);
		int remainTime = fYml.getInt("cataclysm.remain-time", 900);
		long lastResetTime = fYml.getLong("cataclysm.last-reset", Main.getInst().getCalendar().getNextCallsOfEvents().get("reset_mines") - (1000*60*60*24));
		
		CataclysmMiner catMiner = new CataclysmMiner(maxTime, remainTime, lastResetTime, p);
		
		int tanalorrLevel = fYml.getInt("tanalorr.level", 0);
		int tanalorrDiggedOres = fYml.getInt("tanalorr.ores", 0);
		
		TanalorrMiner tanMiner = new TanalorrMiner(p, tanalorrLevel, tanalorrDiggedOres);
		
		PlayerMiner miner = new PlayerMiner(p, catMiner, tanMiner);
		return miner;
	}
	
	public static void savePlayer(PlayerMiner miner) {
		Player p = miner.getPlayer();
		File pFile = new File(playerDir, p.getUniqueId().toString()+".yml");
		if(!pFile.exists())
			try {
				pFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(pFile);
		
		fYml.set("cataclysm.max-time", miner.getCatMiner().getMaxTime());
		fYml.set("cataclysm.remain-time", miner.getCatMiner().getRemainTime());
		fYml.set("cataclysm.last-reset", miner.getCatMiner().getLastResetTime());
		
		fYml.set("tanalorr.level", miner.getTanMiner().getLevel());
		fYml.set("tanalorr.ores", miner.getTanMiner().getDiggedOres());
		
		try {
			fYml.save(pFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
