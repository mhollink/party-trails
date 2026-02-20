package dev.hollink.pmtt.runetime.steps;

import dev.hollink.pmtt.model.TrailStep;
import dev.hollink.pmtt.runetime.events.InteractionEvent;
import net.runelite.api.coords.WorldPoint;

public abstract class InteractionStep extends TrailStep {

    protected final InteractionEvent target;

    protected InteractionStep(WorldPoint location, String hint, InteractionEvent target) {
        super(location, hint);
        this.target = target;
    }

    public abstract boolean isFulfilled(InteractionEvent event);
}
