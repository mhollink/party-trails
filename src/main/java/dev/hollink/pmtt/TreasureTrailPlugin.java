package dev.hollink.pmtt;

import com.google.inject.Provides;
import dev.hollink.pmtt.data.events.ClueEventFactory;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Party Trails", description = "Create and complete custom player made treasure trails", tags = {"clue", "treasure", "custom"})
public class TreasureTrailPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private TreasureTrailConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private TrailManager trailManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Treasure Trail plugin started");
		trailManager.start();
		resumeOrStartTrailFromConfig();
	}

	@Override
	protected void shutDown() throws Exception
	{
		trailManager.stop();
		log.info("Treasure Trail plugin stopped");
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		ClueEventFactory.fromAnimationChanged(event, client)
			.ifPresent(clueEvent -> {
				log.debug("Publishing Animation Event {}", clueEvent);
				trailManager.getClueEventBus().publish(clueEvent);
			});
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		ClueEventFactory.fromMenuOptionClicked(event, client)
			.ifPresent(clueEvent -> {
				log.debug("Publishing Interaction Event {}", clueEvent);
				trailManager.getClueEventBus().publish(clueEvent);
			});
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		ClueEventFactory.fromStatChanged(event, client)
			.ifPresent(clueEvent -> {
				log.debug("Publishing Skill Event {}", clueEvent);
				trailManager.getClueEventBus().publish(clueEvent);
			});
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(TreasureTrailConfig.CONFIG_GROUP))
		{
			return;
		}

		if (event.getKey().equals(TreasureTrailConfig.TREASURE_TRAIL))
		{
			String trailString = configManager.getConfiguration(TreasureTrailConfig.CONFIG_GROUP, TreasureTrailConfig.TREASURE_TRAIL);
			trailManager.startTrail(trailString);
		}
	}

	@Provides
	TreasureTrailConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TreasureTrailConfig.class);
	}

	private void resumeOrStartTrailFromConfig()
	{
		boolean hasTrailConfig = config.trailString() != null && !config.trailString().isBlank();
		boolean hasStoredProgress = config.trailProgress() != null && !config.trailProgress().isBlank();

		log.info("Attempting to resume trail from config... (hasTrail={}, hasProgress={})", hasTrailConfig, hasStoredProgress);
		if (hasTrailConfig)
		{
			if (hasStoredProgress)
			{
				trailManager.resumeTrail(config.trailString(), config.trailProgress());
			}
			else
			{
				trailManager.startTrail(config.trailString());
			}
		}
	}
}
