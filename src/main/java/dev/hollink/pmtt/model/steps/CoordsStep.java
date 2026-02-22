package dev.hollink.pmtt.model.steps;

import dev.hollink.pmtt.model.StepTypes;
import dev.hollink.pmtt.model.trail.ClueContext;
import dev.hollink.pmtt.model.events.ClueEvent;
import dev.hollink.pmtt.model.trail.Encodable;
import dev.hollink.pmtt.model.events.AnimationEvent;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;

@RequiredArgsConstructor
public final class CoordsStep implements TrailStep {

    private final String hint;
    private final WorldPoint targetLocation;

    @Override
    public byte typeId() {
        return StepTypes.COORDINATE_STEP;
    }

    @Override
    public void drawOverlay(PanelComponent panel, Graphics2D graphics) {
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
    public void onActivate(ClueContext context) {

    }

    @Override
    public boolean isComplete(ClueEvent event) {
        if (event instanceof AnimationEvent animationEvent) {
            return animationEvent.animationId() == HUMAN_DIG
                && isInRange(targetLocation, animationEvent.location());
        } else {
            return false;
        }
    }

    @Override
    public void encode(DataOutput out) throws IOException {

    }

    @Override
    public Encodable decode(DataInput in) throws IOException {
        return null;
    }
}
