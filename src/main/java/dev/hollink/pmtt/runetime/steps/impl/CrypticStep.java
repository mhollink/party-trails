package dev.hollink.pmtt.runetime.steps.impl;

import dev.hollink.pmtt.model.TrailStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

// Interact with a specific NPC or Object
public class CrypticStep extends TrailStep {

    public CrypticStep(WorldPoint location, String hint) {
        super(location, hint);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {

    }

}
