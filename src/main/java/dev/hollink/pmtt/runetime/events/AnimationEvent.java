package dev.hollink.pmtt.runetime.events;

import net.runelite.api.coords.WorldPoint;

public record AnimationEvent(WorldPoint location, int animation) {
}
