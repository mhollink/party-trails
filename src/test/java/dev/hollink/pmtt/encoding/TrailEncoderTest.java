package dev.hollink.pmtt.encoding;

import dev.hollink.pmtt.data.Emote;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.AnagramStep;
import dev.hollink.pmtt.data.steps.CipherStep;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.CrypticStep;
import dev.hollink.pmtt.data.steps.EmoteStep;
import dev.hollink.pmtt.data.steps.SkillStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.util.List;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
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

		assertThat(decodedTrail.getMetadata().version(), equalTo(trail.getMetadata().version()));
		assertThat(decodedTrail.getMetadata().trailId(), equalTo(trail.getMetadata().trailId()));
		assertThat(decodedTrail.getMetadata().trailName(), equalTo(trail.getMetadata().trailName()));
		assertThat(decodedTrail.getMetadata().author(), equalTo(trail.getMetadata().author()));

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
				new EmoteStep("Wave in the door opening of the Lumbridge Castle kitchen.",
					Emote.WAVE,
					new WorldPoint(3208, 3212, 0)),
				new CipherStep("QRES ENRA RKN RMABEO RUG CHXPVC",
					new InteractionTarget(
						5581,
						"Logs",
						"Take-axe",
						new WorldPoint(3186, 3277, 0)
					)),
				new CoordsStep(
					"4 degrees 26 minutes north\n21 degrees 33 minutes east",
					new WorldPoint(3130, 3303, 0)),
				new AnagramStep(
					"AI SOLD WOMEN",
					new InteractionTarget(
						2109,
						"Wise Old Man",
						"Talk-to",
						new WorldPoint(3088, 3255, 0)
					)),
				new CrypticStep(
					"Where darkness drips beneath the bog’s slow breath,\n" +
						"Seek light that fears the cavern’s yawning depth.\n" +
						"Among the reeds where lost flames waver and sigh,\n" +
						"Find the keeper of warmth before shadows pass by.",
					new InteractionTarget(
						1896,
						"Candle seller",
						"Talk-to",
						new WorldPoint(3170, 3176, 0)
					)),
				new SkillStep(
					"Mine 5 ores in the south of Lumbridge",
					Skill.MINING,
					75,
					new WorldArea(3220, 3140, 12, 10, 0)
				)
			)
		);
	}
}