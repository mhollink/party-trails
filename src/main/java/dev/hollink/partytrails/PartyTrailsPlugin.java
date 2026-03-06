package dev.hollink.partytrails;

import com.google.inject.Provides;
import dev.hollink.partytrails.builder.TrailBuilderOverlay;
import dev.hollink.partytrails.builder.TrailBuilderPanel;
import dev.hollink.partytrails.data.events.ClueEventFactory;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.runetime.TrailEventBus;
import dev.hollink.partytrails.runetime.TrailManager;
import dev.hollink.partytrails.runetime.TrailOverlay;
import dev.hollink.partytrails.runetime.TrailRuntime;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
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
 * to an internal {@link TrailEvent} which is created using the
 * {@link ClueEventFactory}.
 * <p>
 * These events are then passed through the {@link TrailManager},
 * over the {@link TrailEventBus} to the {@link TrailRuntime}. The runtime
 * keeps track of the active step and checks if a step is completed using
 * the {@link TrailStep#isComplete(TrailContext, TrailEvent)}.
 * <p>
 * The progress along the active trail gets automatically stored using
 * the configmanager to ensure players can continue after logging.
 */
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
	private ClientToolbar clientToolbar;

	@Inject
	private PartyTrailsConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private TrailManager trailManager;

	@Inject
	private TrailEventBus clueEventBus;

	@Inject
	private  OverlayManager overlayManager;

	@Inject
	private  TrailOverlay trailOverlay;


	private NavigationButton navButton;
	private TrailBuilderPanel builderPanel;
	private TrailBuilderOverlay builderOverlay;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Treasure Trail plugin started");
		builderPanel = new TrailBuilderPanel(client, configManager, clueEventBus);
		builderOverlay = new TrailBuilderOverlay(client, config, builderPanel);
		navButton = createNavButton(builderPanel);
		clientToolbar.addNavigation(navButton);

		trailManager.start();
		overlayManager.add(trailOverlay);
		overlayManager.add(builderOverlay);
		resumeOrStartTrailFromConfig();
	}

	@Override
	protected void shutDown() throws Exception
	{
		trailManager.stop();
		overlayManager.remove(trailOverlay);
		overlayManager.remove(builderOverlay);
		clientToolbar.removeNavigation(navButton);
		log.debug("Treasure Trail plugin stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (GameState.LOADING.equals(gameStateChanged.getGameState()))
		{
			return;
		}

		if (GameState.LOGGED_IN.equals(gameStateChanged.getGameState()))
		{
			builderPanel.enableTrailBuilder();
		}
		else
		{
			builderPanel.disableTrailBuilder();
		}
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
			String trailString = configManager.getConfiguration(PartyTrailsConfig.CONFIG_GROUP, PartyTrailsConfig.TREASURE_TRAIL);
			trailManager.startTrail(trailString);
		}
	}

	@Provides
	PartyTrailsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PartyTrailsConfig.class);
	}

	private void resumeOrStartTrailFromConfig()
	{
		boolean hasTrailConfig = config.trailString() != null && !config.trailString().isBlank();
		boolean hasStoredProgress = config.trailProgress() != null && !config.trailProgress().isBlank();

		log.debug("Attempting to resume trail from config... (hasTrail={}, hasProgress={})", hasTrailConfig, hasStoredProgress);
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

	private NavigationButton createNavButton(TrailBuilderPanel builderPanel)
	{
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		return NavigationButton.builder()
			.tooltip("Party Trail Builder")
			.icon(icon)
			.priority(5)
			.panel(this.builderPanel)
			.build();

	}

}
