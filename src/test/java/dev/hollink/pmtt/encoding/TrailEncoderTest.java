package dev.hollink.pmtt.encoding;

import dev.hollink.pmtt.data.Emote;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.AnagramStep;
import dev.hollink.pmtt.data.steps.CipherStep;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.CrypticStep;
import dev.hollink.pmtt.data.steps.EmoteStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;
import net.runelite.api.coords.WorldPoint;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TrailEncoderTest
{

	@Test
	public void encode() throws Exception
	{
		TreasureTrail trail = createTestData();

		String encodedTrail = TrailEncoder.encode(trail);
		TreasureTrail decodedTrail = TrailDecoder.decode(encodedTrail);

		assertThat( decodedTrail.getMetadata().version(), equalTo(trail.getMetadata().version()));
		assertThat( decodedTrail.getMetadata().trailId(), equalTo(trail.getMetadata().trailId()));
		assertThat( decodedTrail.getMetadata().trailName(), equalTo(trail.getMetadata().trailName()));
		assertThat( decodedTrail.getMetadata().author(), equalTo(trail.getMetadata().author()));

		for (int i = 0; i < trail.getMetadata().stepCount(); i++)
		{
			TrailStep trailStep = trail.getStep(i).orElseThrow();
			TrailStep decodedStep = decodedTrail.getStep(i).orElseThrow();

			assertThat(decodedStep, equalTo(trailStep));
		}
	}


	private TreasureTrail createTestData()
	{
		return new TreasureTrail(
			69,
			"random id",
			"Test trail",
			"mhollink",
			List.of(
				new EmoteStep("Perform a dance next to Merlin",
					Emote.DANCE,
					new WorldPoint(234, 789, 1)),
				new CipherStep("RIEV E PSRI XIEO XVII",
					new InteractionTarget(
						123,
						"Teak tree",
						"Cut",
						new WorldPoint(796, 123, 0)
					)),
				new AnagramStep(
					"AI SOLD WOMEN",
					new InteractionTarget(
						123,
						"Wise Old Man",
						"Talk-to",
						new WorldPoint(342, 867, 0)
					)),
				new CoordsStep(
					"6 degrees 33 minutes north\n12 degrees 39 minutes east",
					new WorldPoint(234, 789, 0)),
				new CrypticStep(
					"Command a band in turmoil, backing a wager before the wave.",
					new InteractionTarget(
						123,
						"Commander Connad",
						"Talk-to",
						new WorldPoint(69, 420, 0)
					))
			)
		);
	}
}