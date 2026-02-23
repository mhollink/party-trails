package dev.hollink.pmtt;

import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.encoding.InvalidMagicHeaderException;
import dev.hollink.pmtt.encoding.TrailDecoder;
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
			TreasureTrail treasureTrail = TrailDecoder.decode(encodedTrail);
			trailRuntime.startTrail(treasureTrail);
		}
		catch (InvalidMagicHeaderException e)
		{
			log.warn("Invalid magic header in treasure trail");
		}
		catch (IOException e)
		{
			log.error("Error while decoding treasure trail!", e);
		}
	}
}
