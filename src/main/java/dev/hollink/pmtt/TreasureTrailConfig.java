package dev.hollink.pmtt;

import static dev.hollink.pmtt.TreasureTrailConfig.CONFIG_GROUP;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CONFIG_GROUP)
public interface TreasureTrailConfig extends Config
{
	String CONFIG_GROUP = "PlayerMadeTreasureTrail";

	String TREASURE_TRAIL = "TreasureTrail";
	String TREASURE_TRAIL_PROGRESS = "TreasureTrailProgress";

	//	@ConfigSection(
//		name = "Player",
//		description = "Player configuration",
//		position = 0
//	)
	String player = "player";

	@ConfigItem(
		keyName = TREASURE_TRAIL,
		name = "Active Treasure Trail",
		description = "Encoded treasure trail string from the clue builder panel.",
		section = player
	)
	default String trailString()
	{
		return "";
	}

	@ConfigItem(
		keyName = TREASURE_TRAIL_PROGRESS,
		name = "Trail Progress",
		description = "Encoded progress along the given `trailString`.",
		section = player,
		hidden = true)
	default String trailProgress()
	{
		return "";
	}
}
