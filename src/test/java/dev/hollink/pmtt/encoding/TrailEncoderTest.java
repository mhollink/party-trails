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
		"eJxNUj1vFDEQHSAkx-qCaKiZhi6JlIQggWiOy_Kh3AfsHRytN55bW-u1V7aPJR0lBSUNv4RfcX-" +
			"Chr_BeCVOFE-a9cx78_F2WUxOASCHe15Y6RrUErIlhYjRC21g0ChnjLY17N-CZyvxmVBbjIp" +
			"QOufRtWS1rdCt-7fJpim9lhXhWIRoCGsdrxXZE24xABh-Y3znGG7Do_dFvsB8VoywuJphMR2" +
			"9zOdYfHiN4zef3n0cAzzcwt7EVQEGS1HTsfhCzPaMbVK4AxdPUFLliQKePcVG203k0DofVXZ" +
			"2usudn-9yxEMx_znjT9LYh8PRW1zMJ5e4mk_zGQ_5AoYrHQjnRuJUWDhYClMfR8eUB4xfibY" +
			"H25UizycQvrYUAkqv24AlWRJR9ZcoXfX768-AwbgOS5_ej7IFUY1GV4qvq0TENQkf-vJrPqy" +
			"3PeNGdP1JJbVRnWSjxvFHquFtZMCu72wcO7Q2ouGtusRFdg8DSx9lrzSHiVATtZxhbzrhGx6" +
			"spLVjclBCui5gK3j08obNOVBwOGYFdiyQMeT_37tkqLT3XXg81ZbwAlkl_PsPgtuwNDfZmc-" +
			"l9xlXzPvBuOR4yMiSxl85qLSz";

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
					2108,
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
				))
		)
	);

	@Test
	public void encode() throws Exception
	{
		String encodedTrail = TrailEncoder.encodeTrail(TRAIL);

		assertThat(encodedTrail, equalTo(ENCODED_TRAIL));
	}

	@Test
	public void decode() throws Exception
	{
		TreasureTrail decodedTrail = TrailDecoder.decodeTrail(ENCODED_TRAIL);

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