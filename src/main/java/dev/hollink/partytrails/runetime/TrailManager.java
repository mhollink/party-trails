package dev.hollink.partytrails.runetime;

import dev.hollink.partytrails.codec.TrailCodec;
import dev.hollink.partytrails.codec.TrailProgressCodec;
import dev.hollink.partytrails.codec.exceptions.InvalidMagicHeaderException;
import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.trail.TrailProgress;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailManager
{
	private final OverlayManager overlayManager;
	private final TrailOverlay trailOverlay;
	private final TrailRuntime trailRuntime;
	private final TrailCodec trailCodec;
	private final TrailProgressCodec progressCodec;

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
			TreasureTrail treasureTrail = trailCodec.decode(encodedTrail);
			trailRuntime.startTrail(treasureTrail);
		}
		catch (InvalidMagicHeaderException e)
		{
			log.warn("Invalid magic header in treasure trail");
			trailRuntime.reset();
		}
		catch (Exception e)
		{
			log.error("Error while decoding treasure trail to start! {}, {}", e.getClass().getSimpleName(), e.getMessage());
			trailRuntime.reset();
		}
	}

	public void resumeTrail(String encodedTrail, String encodedProgress)
	{
		try
		{
			TreasureTrail treasureTrail = trailCodec.decode(encodedTrail);
			TrailProgress trailProgress = progressCodec.decode(encodedProgress);

			if (trailProgress.getTrailId().equals(treasureTrail.getMetadata().getTrailId()))
			{
				trailRuntime.resumeTrail(treasureTrail, trailProgress);
			}
			else
			{
				log.warn("Trail id in progress did not match trail id in treasure trail, cancelling resume...");
				trailRuntime.startTrail(treasureTrail);
			}
		}
		catch (Exception e)
		{
			log.error("Error while decoding treasure trail to resume! {}, {}", e.getClass().getSimpleName(), e.getMessage());
		}
	}
}
