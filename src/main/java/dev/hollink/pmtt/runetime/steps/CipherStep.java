package dev.hollink.pmtt.runetime.steps;

import dev.hollink.pmtt.model.TrailStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

// Decipher the hidden message and interact
// with the NPC or object.
public class CipherStep extends TrailStep {

    public CipherStep(WorldPoint location, String hint) {
        super(location, hint);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {

    }

}
