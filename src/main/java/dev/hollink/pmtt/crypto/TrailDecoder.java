package dev.hollink.pmtt.crypto;

import dev.hollink.pmtt.model.Emote;
import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.model.steps.CoordsStep;
import dev.hollink.pmtt.model.steps.EmoteStep;
import dev.hollink.pmtt.model.steps.TrailStep;
import java.io.DataInput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

@Slf4j
public class TrailDecoder
{
	public TreasureTrail decode(String encodedTrail)
	{
		// TODO: Create actual decoder impl.
		log.debug("Decoded Trail: {}", encodedTrail);
		List<TrailStep> trailSteps = List.of(
			new EmoteStep("In front of the Lumbrige Castle.", Emote.WAVE, new WorldPoint(3221, 3219, 0)),
			new CoordsStep("00 degrees 20 minutes south,\n23 degrees 15 minutes east", new WorldPoint(3201, 3169, 0)));
		TreasureTrail treasureTrail = TreasureTrail.builder()
			.version(1)
			.trailId(UUID.randomUUID().toString())
			.trailName("Hardcoded test trail")
			.author("mhollink")
			.steps(trailSteps)
			.build();
		log.debug("Trail decoded with {} trail steps", trailSteps.size());
		return treasureTrail;
	}

	public static String readString(DataInput in) throws IOException
	{
		int length = in.readUnsignedShort();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
