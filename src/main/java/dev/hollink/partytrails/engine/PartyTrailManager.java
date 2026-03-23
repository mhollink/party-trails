package dev.hollink.partytrails.engine;

import dev.hollink.partytrails.PartyTrailsConfig;
import dev.hollink.partytrails.PartyTrailsPlugin;
import dev.hollink.partytrails.codec.TrailCodec;
import dev.hollink.partytrails.engine.builder.TrailBuilderOverlay;
import dev.hollink.partytrails.engine.builder.TrailBuilderPanel;
import dev.hollink.partytrails.engine.hunter.TrailHunterManager;
import dev.hollink.partytrails.engine.hunter.TrailHunterOverlay;
import dev.hollink.partytrails.events.TrailEventBus;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PartyTrailManager
{
	private final PartyTrailsPlugin plugin;
	private final Client client;
	private final ConfigManager configManager;
	private final TrailEventBus eventBus;
	private final PartyTrailsConfig config;
	private final TrailHunterManager hunterManager;
	private final ClientToolbar toolbar;
	private final TrailCodec trailCodec;
	private final OverlayManager overlayManager;

	private TrailHunterOverlay hintsOverlay;
	private TrailBuilderOverlay builderOverlay;
	private TrailBuilderPanel panel;
	private NavigationButton navButton;

	public void start()
	{
		log.debug("Treasure Trail plugin started");

		panel = new TrailBuilderPanel(client, configManager, eventBus, trailCodec);
		updateBuilderPanelVisibility();

		overlayManager.add(hintsOverlay = new TrailHunterOverlay(plugin, hunterManager.getTrailEngine()));
		overlayManager.add(builderOverlay = new TrailBuilderOverlay(client, config, panel));
	}

	public void stop()
	{
		toolbar.removeNavigation(navButton);

		overlayManager.remove(hintsOverlay);
		overlayManager.remove(builderOverlay);
		log.debug("Treasure Trail plugin stopped");
	}

	public void resumeOrStartTrailFromConfig()
	{
		boolean hasTrailConfig = config.trailString() != null && !config.trailString().isBlank();
		boolean hasStoredProgress = config.trailProgress() != null && !config.trailProgress().isBlank();

		log.debug("Attempting to resume trail from config... (hasTrail={}, hasProgress={})", hasTrailConfig, hasStoredProgress);
		if (hasTrailConfig)
		{
			if (hasStoredProgress)
			{
				hunterManager.resumeTrail(config.trailString(), config.trailProgress());
			}
			else
			{
				hunterManager.startTrail(config.trailString());
			}
		}
		else
		{
			hunterManager.stopTrial();
		}
	}

	public void updateBuilderPanelVisibility()
	{
		boolean shown = config.showBuilderPanel();
		if (shown)
		{
			toolbar.addNavigation(navButton = createNavButton(panel));
		}
		else
		{
			toolbar.removeNavigation(navButton);
		}
	}

	private NavigationButton createNavButton(TrailBuilderPanel panel)
	{
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		return NavigationButton.builder()
			.tooltip("Party Trail Builder")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();
	}

	public void onGamestateChanged(GameState gameState)
	{
		if (GameState.LOADING == gameState)
		{
			return;
		}

		if (GameState.LOGGED_IN == gameState)
		{
			resumeOrStartTrailFromConfig();
			panel.enableTrailBuilder();
		}
		else
		{
			panel.disableTrailBuilder();
		}
	}
}
