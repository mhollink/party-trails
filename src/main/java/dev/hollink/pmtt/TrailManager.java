package dev.hollink.pmtt;

import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.trail.TrailProgress;
import dev.hollink.pmtt.encoding.InvalidMagicHeaderException;
import dev.hollink.pmtt.encoding.TrailDecoder;
import dev.hollink.pmtt.overlay.TrailOverlay;
import dev.hollink.pmtt.runetime.TrailRuntime;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayManager;

/**
 * The TrailManager is in charge of starting or resuming a trail
 * using the {@link TrailManager#startTrail(String)} and
 * {@link TrailManager#resumeTrail(String, String)}.
 * <p>
 * Taking the encoded binary string as input, it uses the
 * {@link TrailDecoder} functions to convert it back into a
 * {@link TreasureTrail} and/or {@link TrailProgress}.
 * <p>
 * The decoded trails are passed to the start/resume functions
 * on the {@link TrailRuntime}.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailManager
{
	private final OverlayManager overlayManager;
	private final TrailOverlay trailOverlay;
	private final TrailRuntime trailRuntime;

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

	public void resumeTrail(String encodedTrail, String encodedProgress)
	{
		try
		{
			TreasureTrail treasureTrail = TrailDecoder.decodeTrail(encodedTrail);
			TrailProgress trailProgress = TrailDecoder.decodeProgress(encodedProgress);

			if (trailProgress.getTrailId().equals(treasureTrail.getMetadata().trailId()))
			{
				trailRuntime.resumeTrail(treasureTrail, trailProgress);
			}
			else
			{
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
