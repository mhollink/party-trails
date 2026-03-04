package dev.hollink.partytrails.data.events;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class InteractionEvent implements TrailEvent
{
	int objectId;
	String objectName;
	String action;
	WorldPoint location;
}