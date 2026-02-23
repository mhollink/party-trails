package dev.hollink.pmtt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("TreasureTrailPlugin.PlayerConfig")
public interface TreasureTrailConfig extends Config
{
	@ConfigItem(keyName = "trailString", name = "Trail String", description = "Encoded treasure trail")
	default String trailString()
	{
		return "";
	}
}
