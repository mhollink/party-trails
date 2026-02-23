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
	public static final String ENCODED_TRAIL =
		"eJxNUj1vFDEQHSAkx-qCaKiZhi6JlIQggURxXJYP5T5g7-BovfHc2lqvvbJ9LOkoKShp-" +
			"CX8ivsTNPwNxitxonjSrGfem4-3y2JyCgA53PPCSteglpAtKUSMXmgDg0Y5Y7StYf8WPFu" +
			"Jz4TaYlSE0jmPriWrbYVu3b9NNk3ptawIxyJEQ1jreK3InnCLAcDwG-M7x3AbHr0v8g" +
			"Xms2KExdUMi-noZT7H4sNrHL_59O7jGODhFvYmrgowWIqajsUXYrZnbJPCHbh4gpIqT" +
			"xTw7Ck22m4ih9b5qLKz013u_HyXIx6K-c8Zf5LGPhyO3uJiPrnE1Xyaz3jIFzBc6UA4N" +
			"xKnwsLBUpj6ODqmPGD8SrQ92K4UeT6B8LWlEFB63QYsyZKIqr9E6arfX38GDMZ1WPr0f" +
			"pQtiGo0ulJ8XSUirkn40Jdf82G97Rk3outPKqmN6iQbNY4_Ug1vIwN2fWfj2KG1EQ1v1S" +
			"UusnsYWPooe6U5TISaqOUMe9MJ3_BgJa0dk4MS0nUBW8GjlzdszoGCwzErsGOBjCH__94l" +
			"Q6W978LjqbaEF8gq4d9_ENyGpbnJznwuvc-4Yt4PxiXHQ0aWNP4COwi0tA";

	public static final TreasureTrail TRAIL = new TreasureTrail(
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

	@Test
	public void encode() throws Exception
	{
		String encodedTrail = TrailEncoder.encode(TRAIL);

		assertThat(encodedTrail, equalTo(ENCODED_TRAIL));
	}

	@Test
	public void decode() throws Exception
	{
		TreasureTrail decodedTrail = TrailDecoder.decode(ENCODED_TRAIL);

		assertThat(decodedTrail.getMetadata().version(), equalTo(TRAIL.getMetadata().version()));
		assertThat(decodedTrail.getMetadata().trailId(), equalTo(TRAIL.getMetadata().trailId()));
		assertThat(decodedTrail.getMetadata().trailName(), equalTo(TRAIL.getMetadata().trailName()));
		assertThat(decodedTrail.getMetadata().author(), equalTo(TRAIL.getMetadata().author()));

		for (int i = 0; i < TRAIL.getMetadata().stepCount(); i++)
		{
			TrailStep trailStep = TRAIL.getStep(i).orElseThrow();
			TrailStep decodedStep = decodedTrail.getStep(i).orElseThrow();

			assertThat(decodedStep, equalTo(trailStep));
		}
	}
}