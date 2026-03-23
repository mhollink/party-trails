package dev.hollink.partytrails.engine.hunter;

import dev.hollink.partytrails.codec.TrailCodec;
import dev.hollink.partytrails.codec.TrailProgressCodec;
import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.data.trail.TrailProgress;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TrailHunterManager
{
	private final Client client;
	private final TrailCodec trailCodec;
	private final TrailProgressCodec progressCodec;

	@Getter
	private final TrailHunterEngine trailEngine;

	public void startTrail(String encodedTrail)
	{
		if (encodedTrail == null || encodedTrail.isEmpty())
		{
			return;
		}

		try
		{
			TreasureTrail treasureTrail = trailCodec.decode(encodedTrail);
			trailEngine.start(treasureTrail, new TrailContext(client));
		}
		catch (Exception e)
		{
			log.error("Error while decoding treasure trail to start! {}, {}", e.getClass().getSimpleName(), e.getMessage());
			trailEngine.reset();
		}
	}

	public void resumeTrail(String encodedTrail, String encodedProgress)
	{
		TreasureTrail treasureTrail;
		TrailProgress trailProgress;

		try
		{
			treasureTrail = trailCodec.decode(encodedTrail);
		}
		catch (Exception e)
		{
			log.error("Error while decoding treasure trail to resume! {}, {}", e.getClass().getSimpleName(), e.getMessage());
			return;
		}

		try
		{
			trailProgress = progressCodec.decode(encodedProgress);
		}
		catch (Exception e)
		{
			log.error("Error while decoding progress to resume with! {}, {}", e.getClass().getSimpleName(), e.getMessage());
			trailEngine.start(treasureTrail, new TrailContext(client));
			return;
		}

		log.debug("Resuming trail {} with progress...", treasureTrail.getMetadata().getTrailName());
		trailEngine.start(treasureTrail, new TrailContext(client, trailProgress));
	}

	public void stopTrial() {
		trailEngine.reset();
	}
}
