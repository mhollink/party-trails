package dev.hollink.partytrails;

import static dev.hollink.partytrails.PartyTrailsConfig.CONFIG_GROUP;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(CONFIG_GROUP)
public interface PartyTrailsConfig extends Config
{
	String CONFIG_GROUP = "PartyTrailsPlugin";

	String TREASURE_TRAIL = "TreasureTrail";
	String TREASURE_TRAIL_PROGRESS = "TreasureTrailProgress";
	String SHOW_BUILDER_PANEL = "ShowBuilderPanel";
	String SHOW_BUILDER_OVERLAY = "ShowBuilderOverlay";
	String BUILDER_OVERLAY_COLOR = "BuilderOverlayColor";
	String BUILDER_STATE = "SavedBuilderState";

	@ConfigSection(
		name = "Player",
		description = "Player trail configuration",
		position = 0
	)
	String player = "Player";

	@ConfigItem(
		keyName = TREASURE_TRAIL,
		name = "Active Treasure Trail",
		description = "Encoded treasure trail string from the clue builder panel.",
		position = 0,
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

	@ConfigSection(
		name = "Builder",
		description = "Builder configuration",
		position = 1,
		closedByDefault = true
	)
	String builder = "Builder";

	@ConfigItem(
		keyName = SHOW_BUILDER_PANEL,
		name = "Show builder panel",
		description = "Add the sidepanel in which you can configure your custom Treasure Trials.",
		position = 0,
		section = builder)
	default boolean showBuilderPanel()
	{
		return false;
	}

	@ConfigItem(
		keyName = SHOW_BUILDER_OVERLAY,
		name = "Highlight targets & tiles",
		description = "Show the targeted tiles, NPCs and areas marked inside the trail builder.",
		position = 1,
		section = builder)
	default boolean showBuilderOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = BUILDER_OVERLAY_COLOR,
		name = "Highlight color",
		description = "The color of the builder overlay",
		position = 2,
		section = builder)
	default Color builderOverlayColor()
	{
		return new Color(193, 121, 255);
	}

	@ConfigItem(
		keyName = BUILDER_STATE,
		name = "Builder save",
		description = "Saved form state from the clue builder which can be reloaded.",
		position = 4,
		section = builder)
	default String builderState()
	{
		return "";
	}
}
