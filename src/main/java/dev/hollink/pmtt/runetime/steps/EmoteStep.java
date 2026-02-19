package dev.hollink.pmtt.runetime.steps;

import dev.hollink.pmtt.model.Emote;
import dev.hollink.pmtt.model.TrailStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;

public class EmoteStep extends TrailStep implements AnimationStep {

    private final Emote emote;

    public EmoteStep(WorldPoint location, String hint, Emote emote) {
        super(location, hint);
        this.emote = emote;
    }

    @Override
    public void showStepOverlay(PanelComponent panel, Graphics2D graphics) {
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = Math.max(
            ComponentConstants.STANDARD_WIDTH,
            fontMetrics.stringWidth(hint) + 10);

        panel.setPreferredSize(new Dimension(textWidth, 0));

        panel.getChildren().add(TitleComponent.builder().text("Emote Clue").build());
        panel.getChildren().add(LineComponent.builder().left("Emotes:").build());
        panel.getChildren().add(LineComponent.builder()
            .left(emote.getName())
            .leftColor(TITLED_CONTENT_COLOR)
            .build());

        panel.getChildren().add(LineComponent.builder().left("Location:").build());
        panel.getChildren().add(LineComponent.builder()
            .left(hint)
            .leftColor(TITLED_CONTENT_COLOR)
            .build());
    }

    @Override
    public boolean isFulfilled(WorldPoint eventLocation, int animationId) {
        return emote.getAnimationId() == animationId && location.distanceTo(eventLocation) <= DEFAULT_LOCATION_DISTANCE;
    }
}
