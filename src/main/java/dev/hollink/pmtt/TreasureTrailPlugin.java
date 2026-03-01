package dev.hollink.pmtt;

import com.google.inject.Provides;
import dev.hollink.pmtt.builder.TrailBuilderPanel;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.events.ClueEventFactory;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.TrailContext;
import dev.hollink.pmtt.runetime.TrailEventBus;
import dev.hollink.pmtt.runetime.TrailRuntime;
import java.awt.image.BufferedImage;
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
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

/**
 * Party Trails is a plugin that allows players to create custom
 * treasure-trails which consist of multiple different steps. The
 * plugin converts these steps in a base64 encoded binary string
 * which can then be shared amongst players.
 * <p>
 * Players can enter a trail string into the config. This trail
 * gets decoded and loaded up in to the client. The current step
 * descriptions will be shown with the help of overlay panels.
 * Performing the action required for the step automatically
 * advances the player to the next step.
 * <p>
 * Party trails uses the clients {@link AnimationChanged},
 * {@link MenuOptionClicked} and {@link StatChanged} events to help
 * determine if a clue step is finished. These events get converted
 * to an internal {@link ClueEvent} which is created using the
 * {@link ClueEventFactory}.
 * <p>
 * These events are then passed through the {@link TrailManager},
 * over the {@link TrailEventBus} to the {@link TrailRuntime}. The runtime
 * keeps track of the active step and checks if a step is completed using
 * the {@link TrailStep#isComplete(TrailContext, ClueEvent)}.
 * <p>
 * The progress along the active trail gets automatically stored using
 * the configmanager to ensure players can continue after logging.
 */
@Slf4j
@PluginDescriptor(
	name = "Party Trails",
	description = "Create and complete custom player made treasure trails",
	tags = {"clue", "treasure", "party"})
public class TreasureTrailPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private TreasureTrailConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private TrailManager trailManager;

	@Inject
	private TrailEventBus clueEventBus;

	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Treasure Trail plugin started");
		addTrailBuilderPanel();

		trailManager.start();
		resumeOrStartTrailFromConfig();
	}

	@Override
	protected void shutDown() throws Exception
	{
		trailManager.stop();
		clientToolbar.removeNavigation(navButton);
		log.info("Treasure Trail plugin stopped");
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

	private void addTrailBuilderPanel()
	{
		TrailBuilderPanel builderPanel = new TrailBuilderPanel(client, clueEventBus);

		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		navButton = NavigationButton.builder()
			.tooltip("Party Trail Builder")
			.icon(icon)
			.priority(5)
			.panel(builderPanel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

}
