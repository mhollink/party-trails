package dev.hollink.pmtt.runetime.steps.impl;

import dev.hollink.pmtt.model.TrailStep;
import dev.hollink.pmtt.runetime.events.InteractionEvent;
import dev.hollink.pmtt.runetime.steps.InteractionStep;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

@Slf4j
public final class CrypticStep extends InteractionStep {

    public CrypticStep(WorldPoint location, String hint, InteractionEvent target) {
        super(location, hint, target);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {

    }

    @Override
    public boolean isFulfilled(InteractionEvent event) {
        if (target == null) {
            log.error("Target is null, skipping step");
            return true;
        }
        return target.compare(event);
    }
}
