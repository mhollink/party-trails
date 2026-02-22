package dev.hollink.pmtt.model.events;

import net.runelite.api.coords.WorldPoint;

public record InteractionEvent(int objectId, String objectName, String action, WorldPoint location) implements ClueEvent
{

}