package dev.hollink.pmtt.runetime.steps.impl;

import dev.hollink.pmtt.runetime.events.InteractionEvent;
import dev.hollink.pmtt.runetime.events.NpcInteraction;
import dev.hollink.pmtt.runetime.events.ObjectInteraction;
import dev.hollink.pmtt.runetime.steps.InteractionStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

// Decipher the hidden message and interact
// with the NPC or object.
public class CipherStep extends InteractionStep {

    public CipherStep(WorldPoint location, String cipher, InteractionEvent target) {
        super(location, cipher, target);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {

    }

    @Override
    public boolean isFulfilled(InteractionEvent event) {
        if (target == null) {
            return false;
        }
        return target.compare(event);
    }
}
