package dev.hollink.pmtt;

import dev.hollink.pmtt.crypto.TrailDecoder;
import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.overlay.TrailOverlay;
import dev.hollink.pmtt.runetime.TrailRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailManager {

    private final TreasureTrailConfig config;
    private final OverlayManager overlayManager;
    private final TrailOverlay trailOverlay;
    private final TrailDecoder trailDecoder;

    private TrailRuntime activeTrail;

    public void startPartyTrails() {
        overlayManager.add(trailOverlay);

        TreasureTrail trail = trailDecoder.decode("not.a.real.encoded.string");
        activeTrail = new TrailRuntime(trail);
        trailOverlay.setTrail(activeTrail);
    }

    public void stop() {
        overlayManager.remove(trailOverlay);
    }

    public Optional<TrailRuntime> getActiveTrail() {
        return Optional.ofNullable(activeTrail);
    }

}
