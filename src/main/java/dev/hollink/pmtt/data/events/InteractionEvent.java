package dev.hollink.pmtt.data.events;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class InteractionEvent implements ClueEvent
{
	int objectId;
	String objectName;
	String action;
	WorldPoint location;
}