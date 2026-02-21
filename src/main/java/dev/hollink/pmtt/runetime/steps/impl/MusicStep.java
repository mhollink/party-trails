package dev.hollink.pmtt.runetime.steps.impl;

import dev.hollink.pmtt.model.TrailStep;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

@Slf4j
public class MusicStep extends TrailStep {

    public MusicStep(WorldPoint location, String hint) {
        super(location, hint);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {

    }

}
