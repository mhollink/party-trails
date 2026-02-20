package dev.hollink.pmtt.crypto;

import dev.hollink.pmtt.model.Emote;
import dev.hollink.pmtt.model.TrailStep;
import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.runetime.steps.impl.CoordsStep;
import dev.hollink.pmtt.runetime.steps.impl.EmoteStep;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

import java.util.List;
import java.util.UUID;

@Slf4j
public class TrailDecoder {

    public TreasureTrail decode(String encodedTrail) {
        log.debug("Decoded Trail: {}", encodedTrail);
        List<TrailStep> trailSteps = List.of(
            new EmoteStep(
                new WorldPoint(3221, 3219, 0),
                "In front of the Lumbrige Castle.",
                Emote.WAVE
            ),
            new CoordsStep(
                new WorldPoint(3201, 3169, 0),
                "00 degrees 20 minutes south,\n" +
                    "23 degrees 15 minutes east"
            )
        );
        TreasureTrail treasureTrail = new TreasureTrail(
            1,
            UUID.randomUUID().toString(),
            "Party Trail Demo",
            "Hollink",
            trailSteps
        );
        log.debug("Trail decoded with {} trail steps", trailSteps.size());
        return treasureTrail;
    }
}
