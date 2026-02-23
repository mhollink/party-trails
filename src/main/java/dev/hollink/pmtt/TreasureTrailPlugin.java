package dev.hollink.pmtt;

import com.google.inject.Provides;
import static dev.hollink.pmtt.data.events.ClueEventFactory.fromAnimationChanged;
import static dev.hollink.pmtt.data.events.ClueEventFactory.fromMenuOptionClicked;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
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
	private TrailManager trailManager;

	@Override
	protected void startUp() throws Exception
	{
		trailManager.startPartyTrails();
	}

	@Override
	protected void shutDown() throws Exception
	{
		trailManager.stop();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		fromAnimationChanged(event, client)
			.ifPresent(clueEvent -> trailManager.getClueEventBus().publish(clueEvent));
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		fromMenuOptionClicked(event, client)
			.ifPresent(clueEvent -> trailManager.getClueEventBus().publish(clueEvent));
	}

	@Provides
	TreasureTrailConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TreasureTrailConfig.class);
	}
}
