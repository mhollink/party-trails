package dev.hollink.pmtt.runetime.steps;

import net.runelite.api.coords.WorldPoint;

public interface AnimationStep {
    boolean isFulfilled(WorldPoint location, int animationId);
}
