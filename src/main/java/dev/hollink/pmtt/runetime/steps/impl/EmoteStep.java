package dev.hollink.pmtt.runetime.steps.impl;

import dev.hollink.pmtt.model.Emote;
import dev.hollink.pmtt.runetime.events.AnimationEvent;
import dev.hollink.pmtt.runetime.steps.AnimationStep;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;

@Slf4j
public final class EmoteStep extends AnimationStep {

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
    public boolean isFulfilled(AnimationEvent event) {
        return event.animation() == emote.getAnimationId()
            && event.location().distanceTo(location) <= DEFAULT_LOCATION_DISTANCE;
    }
}
