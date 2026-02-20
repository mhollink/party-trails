package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.runetime.events.AnimationEvent;
import dev.hollink.pmtt.runetime.events.InteractionEvent;
import dev.hollink.pmtt.runetime.steps.AnimationStep;
import dev.hollink.pmtt.runetime.steps.InteractionStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void performAnimation(AnimationEvent event) {
        if (isFinished()) {
            return;
        }

        trail.getStep(currentStep)
            .filter(step -> step instanceof AnimationStep)
            .map(step -> (AnimationStep) step)
            .map(step -> step.isFulfilled(event))
            .ifPresent(completedCurrentStep -> {
                if (completedCurrentStep) {
                    currentStep++;
                }
            });
    }


    public void performInteractionStep(InteractionEvent event) {
        if (isFinished()) {
            return;
        }

        trail.getStep(currentStep)
            .filter(step -> step instanceof InteractionStep)
            .map(step -> (InteractionStep) step)
            .map(step -> step.isFulfilled(event))
            .ifPresent(completedCurrentStep -> {
                if (completedCurrentStep) {
                    currentStep++;
                }
            });
    }
}
