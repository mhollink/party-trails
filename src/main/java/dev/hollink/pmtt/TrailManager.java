package dev.hollink.pmtt;

import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.trail.TrailProgress;
import dev.hollink.pmtt.encoding.InvalidMagicHeaderException;
import dev.hollink.pmtt.encoding.TrailDecoder;
import dev.hollink.pmtt.encoding.TrailEncoder;
import dev.hollink.pmtt.overlay.TrailOverlay;
import dev.hollink.pmtt.runetime.EventBus;
import dev.hollink.pmtt.runetime.TrailRuntime;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailManager
{
	private final Client client;
	private final TreasureTrailConfig config;
	private final OverlayManager overlayManager;
	private final TrailOverlay trailOverlay;
	private final TrailRuntime trailRuntime;

	@Getter
	private final EventBus clueEventBus;

	public void start()
	{
		overlayManager.add(trailOverlay);
	}

	public void stop()
	{
		overlayManager.remove(trailOverlay);
	}

	public void startTrail(String encodedTrail)
	{
		if (encodedTrail == null || encodedTrail.isEmpty())
		{
			return;
		}

		try
		{
			TreasureTrail treasureTrail = TrailDecoder.decodeTrail(encodedTrail);
			trailRuntime.startTrail(treasureTrail);
		}
		catch (InvalidMagicHeaderException e)
		{
			log.warn("Invalid magic header in treasure trail");
			trailRuntime.reset();
		}
		catch (IOException e)
		{
			log.error("Error while decoding treasure trail!", e);
			trailRuntime.reset();
		}
	}

	public void resumeTrail(String encodedTrail, String encodedProgress) {
		try
		{
			TreasureTrail treasureTrail = TrailDecoder.decodeTrail(encodedTrail);
			TrailProgress trailProgress = TrailDecoder.decodeProgress(encodedProgress);

			if (trailProgress.getTrailId().equals(treasureTrail.getMetadata().trailId())) {
				trailRuntime.resumeTrail(treasureTrail, trailProgress);
			} else {
				log.warn("Trail id in progress did not match trail id in treasure trail, cancelling resume...");
				trailRuntime.startTrail(treasureTrail);
			}
		}
		catch (IOException e)
		{
			log.error("Error while decoding treasure trail!", e);
		}
	}
}
