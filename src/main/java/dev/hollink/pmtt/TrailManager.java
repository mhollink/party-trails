package dev.hollink.pmtt;

import dev.hollink.pmtt.crypto.TrailDecoder;
import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.model.trail.ClueContext;
import dev.hollink.pmtt.model.trail.TrailProgress;
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
	private final TrailDecoder trailDecoder;

	public void startPartyTrails()
	{
		overlayManager.add(trailOverlay);

		TreasureTrail trail = trailDecoder.decode(config.trailString());
		TrailRuntime runtime = new TrailRuntime(clueEventBus, new ClueContext(client, new TrailProgress()));
		runtime.startTrail(trail);
		trailOverlay.setTrailRuntime(runtime);
	}

	public void stop()
	{
		overlayManager.remove(trailOverlay);
	}
}
