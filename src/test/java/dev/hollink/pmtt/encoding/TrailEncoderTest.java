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
import net.runelite.api.coords.WorldPoint;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TrailEncoderTest
{
	public static final String ENCODED_TRAIL =
		"eJxNUUtuFDEQLSCQoZVIbFhTB0giJQEkEJth6CSI-UDPwLCtjmvaVrvtlu2" +
			"kk12WLLgAJ-EUcwk2XIPqXkQsnlSu8qvPe6tiegwAOTwN5JRv0CjIVh" +
			"wTpkDGwqjR3lrjanj8AN6s6ZrROEyaUXkf0LfsjKvQb4bc9Kopg1EV4" +
			"4Risoy1SZea3ZGMGAHs_RD8lBgewosvRb7EfF6Msfg0x2I2fp8vsPh6" +
			"jpOL75-_TQCeb2Fn6qsIoxXVfEg3LOwg2PYdHsGrl6i4CswRT15jY9x" +
			"VktD5kHR2cnxfOz29r7EsJfy3gr99jyewP_6Iy8X0A64Xs3wuS76Dvb" +
			"WJjAurcEYOdldk68PkhfJM8Lun7cB2rTmIBBRqxzGiCqaNWLJjSnpQo" +
			"vTVn7tfEaP1HZahzx9kS-Yaram0qKsp4YYpxOH7pQgb3MC4pW6QVHGb" +
			"9FE2brw8-j9yjYrYDZOtF4c2lhq5quu5KO5hlNYH2ZmRsCfUzK1UxJu" +
			"OQiOLlbzxQo6alO8itiSrl7dizq6G_Yl0EMciW8vh_7tLge7v_geXrqYX";

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

		assertThat(decodedTrail.getMetadata().getVersion(), equalTo(TRAIL.getMetadata().getVersion()));
		assertThat(decodedTrail.getMetadata().getTrailId(), equalTo(TRAIL.getMetadata().getTrailId()));
		assertThat(decodedTrail.getMetadata().getTrailName(), equalTo(TRAIL.getMetadata().getTrailName()));
		assertThat(decodedTrail.getMetadata().getAuthor(), equalTo(TRAIL.getMetadata().getAuthor()));

		for (int i = 0; i < TRAIL.getMetadata().getStepCount(); i++)
		{
			TrailStep trailStep = TRAIL.getStep(i).orElseThrow();
			TrailStep decodedStep = decodedTrail.getStep(i).orElseThrow();

			assertThat(decodedStep, equalTo(trailStep));
		}
	}
}