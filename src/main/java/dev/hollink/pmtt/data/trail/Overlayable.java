package dev.hollink.pmtt.data.trail;

import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.components.PanelComponent;

public interface Overlayable
{

	void drawOverlay(PanelComponent panel, Graphics2D graphics);
}
