package dev.hollink.pmtt.model.events;

import net.runelite.api.coords.WorldPoint;

public record AnimationEvent(int animationId, WorldPoint location) implements ClueEvent
{
}
