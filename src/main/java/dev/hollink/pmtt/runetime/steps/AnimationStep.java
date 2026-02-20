package dev.hollink.pmtt.runetime.steps;

import dev.hollink.pmtt.model.TrailStep;
import dev.hollink.pmtt.runetime.events.AnimationEvent;
import net.runelite.api.coords.WorldPoint;

public abstract class AnimationStep extends TrailStep {

    protected AnimationStep(WorldPoint location, String hint) {
        super(location, hint);
    }

    public abstract boolean isFulfilled(AnimationEvent event);
}
