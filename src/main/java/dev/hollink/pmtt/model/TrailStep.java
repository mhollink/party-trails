package dev.hollink.pmtt.model;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

public abstract class TrailStep
{
    public static final int DEFAULT_LOCATION_DISTANCE = 3;

    protected final WorldPoint location;
    protected final String hint;

    protected TrailStep(WorldPoint location, String hint) {
        this.location = location;
        this.hint = hint;
    }

    public abstract void showStepOverlay(PanelComponent panel, Graphics2D graphics);
}
