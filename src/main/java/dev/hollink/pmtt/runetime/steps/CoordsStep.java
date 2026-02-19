package dev.hollink.pmtt.runetime.steps;

import dev.hollink.pmtt.model.TrailStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;

// Dig at a location based on the x,y
public class CoordsStep extends TrailStep implements AnimationStep {

    public CoordsStep(WorldPoint location, String hint) {
        super(location, hint);
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        String[] coords = hint.split("\n");

        int textWidth = Math.max(
            ComponentConstants.STANDARD_WIDTH,
            Math.max(
                fontMetrics.stringWidth(coords[0]) + 10,
                fontMetrics.stringWidth(coords[1]) + 10
            ));

        panel.setPreferredSize(new Dimension(textWidth, 0));

        panel.getChildren().add(TitleComponent.builder().text("Coordinate Clue").build());
        panel.getChildren().add(LineComponent.builder().left(coords[0]).build());
        panel.getChildren().add(LineComponent.builder().left(coords[1]).build());
    }

    @Override
    public boolean isFulfilled(WorldPoint eventLocation, int animationId) {
        boolean correctAnimation = animationId == HUMAN_DIG;
        boolean correctLocation = location.distanceTo(eventLocation) <= DEFAULT_LOCATION_DISTANCE;

        return correctAnimation && correctLocation;
    }
}
