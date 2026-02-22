package dev.hollink.pmtt.model.trail;

import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

public interface Overlayable {

    void drawOverlay(PanelComponent panel, Graphics2D graphics);
}
