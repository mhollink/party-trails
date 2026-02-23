package dev.hollink.pmtt;

import dev.hollink.pmtt.encoding.TrailDecoder;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.trail.ClueContext;
import dev.hollink.pmtt.data.trail.TrailProgress;
import dev.hollink.pmtt.overlay.TrailOverlay;
import dev.hollink.pmtt.runetime.EventBus;
import dev.hollink.pmtt.runetime.TrailRuntime;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailManager
{

	private final Client client;
	private final TreasureTrailConfig config;

	private final OverlayManager overlayManager;
	private final TrailOverlay trailOverlay;

	@Getter
	private final EventBus clueEventBus;

	public void startPartyTrails()
	{
		overlayManager.add(trailOverlay);

		// TODO: Fix this codesmell
		TrailRuntime runtime = new TrailRuntime(clueEventBus, new ClueContext(client, new TrailProgress()));

		trailOverlay.setTrailRuntime(runtime);
	}

	public void stop()
	{
		overlayManager.remove(trailOverlay);
	}
}
