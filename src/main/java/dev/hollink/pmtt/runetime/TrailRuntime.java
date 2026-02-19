package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.model.TrailStep;
import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.runetime.steps.AnimationStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TrailRuntime {

    private final TreasureTrail trail;
    private int currentStep = 0;

    public void renderCurrentStep(Graphics2D graphics, PanelComponent panel) {
        trail.getStep(currentStep)
            .ifPresent(step -> step.showStepOverlay(panel, graphics));
    }

    public boolean isFinished() {
        return currentStep > trail.getStepCount();
    }

    public void performAnimation(AnimationChanged event) {
        if (isFinished()) {
            return;
        }

        trail.getStep(currentStep)
            .ifPresentOrElse(
                (step) -> doAnimationStep(step, event),
                this::logStepNotFound
            );
    }

    private void doAnimationStep(TrailStep step, AnimationChanged event) {
        if (step instanceof AnimationStep animationStep) {
            Actor player = event.getActor();
            WorldPoint location = player.getWorldLocation();
            int animation = player.getAnimation();
            log.debug("Player performed {} at {}", animation, location);

            if (!animationStep.isFulfilled(location, animation)) {
                return;
            }

            currentStep++;
            log.info("Player correctly performed {} at {}", animation, location);
        }
    }

    private void logStepNotFound() {
        log.warn("No trail step at index {}", currentStep);
    }
}
