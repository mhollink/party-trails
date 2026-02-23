package dev.hollink.pmtt.data.events;

import net.runelite.api.coords.WorldPoint;

public record AnimationEvent(int animationId, WorldPoint location) implements ClueEvent
{
}
