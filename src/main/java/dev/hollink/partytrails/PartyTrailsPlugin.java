package dev.hollink.partytrails;

import com.google.inject.Provides;
import dev.hollink.partytrails.engine.PartyTrailManager;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.ClueEventFactory;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Party Trails",
	description = "Create and complete custom player made treasure trails",
	tags = {"clue", "treasure", "party"})
public class PartyTrailsPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private PartyTrailManager party;

	@Inject
	private TrailEventBus clueEventBus;

	@Override
	protected void startUp() throws Exception
	{
		party.start();
	}

	@Override
	protected void shutDown() throws Exception
	{
		party.stop();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		ClueEventFactory.fromAnimationChanged(event, client).ifPresent(clueEvent -> {
			log.debug("Publishing Animation Event {}", clueEvent);
			clueEventBus.publish(clueEvent);
		});
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		ClueEventFactory.fromMenuOptionClicked(event, client).ifPresent(clueEvent -> {
			log.debug("Publishing Interaction Event {}", clueEvent);
			clueEventBus.publish(clueEvent);
		});
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		ClueEventFactory.fromStatChanged(event, client).ifPresent(clueEvent -> {
			log.debug("Publishing Skill Event {}", clueEvent);
			clueEventBus.publish(clueEvent);
		});
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(PartyTrailsConfig.CONFIG_GROUP))
		{
			return;
		}

		if (event.getKey().equals(PartyTrailsConfig.TREASURE_TRAIL))
		{
			party.resumeOrStartTrailFromConfig();
		}

		if (event.getKey().equals(PartyTrailsConfig.SHOW_BUILDER_PANEL))
		{
			party.updateBuilderPanelVisibility();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		party.onGamestateChanged(gameStateChanged.getGameState());
	}

	@Provides
	PartyTrailsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PartyTrailsConfig.class);
	}
}
