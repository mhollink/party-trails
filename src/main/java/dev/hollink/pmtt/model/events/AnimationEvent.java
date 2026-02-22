package dev.hollink.pmtt.model.events;

import net.runelite.api.coords.WorldPoint;

public record AnimationEvent(WorldPoint location, int animationId) implements ClueEvent {
}
