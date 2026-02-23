package dev.hollink.pmtt;

import static dev.hollink.pmtt.TreasureTrailConfig.CONFIG_GROUP;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CONFIG_GROUP)
public interface TreasureTrailConfig extends Config
{
	String CONFIG_GROUP = "TreasureTrailPlugin.PlayerConfig";

	String TREASURE_TRAIL = "TreasureTrail";
	String TREASURE_TRAIL_PROGRESS = "TreasureTrailEnabled";

	@ConfigItem(keyName = TREASURE_TRAIL, name = "Trail String", description = "Encoded treasure trail", section = "Player configuration")
	default String trailString()
	{
		return "";
	}

	@ConfigItem(keyName = TREASURE_TRAIL_PROGRESS, name = "Trail String", description = "Encoded progress along an trail", hidden = true)
	default String trailProgress()
	{
		return "";
	}
}
