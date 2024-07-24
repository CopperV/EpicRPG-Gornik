package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public final class TanalorrUpgradesManager {

	private static final TanalorrUpgradesManager inst = new TanalorrUpgradesManager();
	
	private final Map<Integer, ATanalorrUpgrade> luckyUpgrades;
	private final Map<Integer, ATanalorrUpgrade> handleUpgrades;
	private final Map<Integer, ATanalorrUpgrade> edgeUpgrades;
	private final Map<Integer, ATanalorrUpgrade> convenienceUpgrades;
	private final Map<Integer, ATanalorrUpgrade> prospectorUpgrades;
	
	private TanalorrUpgradesManager() {
		luckyUpgrades = new LinkedHashMap<>();
		handleUpgrades = new LinkedHashMap<>();
		edgeUpgrades = new LinkedHashMap<>();
		convenienceUpgrades = new LinkedHashMap<>();
		prospectorUpgrades = new LinkedHashMap<>();
	}
	
	public static final TanalorrUpgradesManager get() {
		return inst;
	}
	
	public void registerUpgrade(ATanalorrUpgrade upgrade) {
		int level = upgrade.getLevel();
		switch(upgrade.getType()) {
			case CONVENIENCE:
				convenienceUpgrades.put(level, upgrade);
				break;
			case EDGE:
				edgeUpgrades.put(level, upgrade);
				break;
			case HANDLE:
				handleUpgrades.put(level, upgrade);
				break;
			case LUCKY:
				luckyUpgrades.put(level, upgrade);
				break;
			case PROSPECTOR:
				prospectorUpgrades.put(level, upgrade);
				break;
		}
	}
	
	public ATanalorrUpgrade getUpgradeByLevel(TanalorrUpgradeType type, int level) {
		switch(type) {
			case CONVENIENCE:
				return convenienceUpgrades.get(level);
			case EDGE:
				return edgeUpgrades.get(level);
			case HANDLE:
				return handleUpgrades.get(level);
			case LUCKY:
				return luckyUpgrades.get(level);
			case PROSPECTOR:
				return prospectorUpgrades.get(level);
			default:
				return null;
		}
	}
	
}
