package dev.hollink.pmtt.data;

import dev.hollink.pmtt.data.events.InteractionEvent;
import static dev.hollink.pmtt.data.steps.TrailStep.DEFAULT_LOCATION_DISTANCE;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class InteractionTarget
{
	int targetId;
	String targetName;
	String interactionType;
	WorldPoint location;

	public boolean checkEvent(InteractionEvent event)
	{
		return event.getObjectId() == targetId
			&& event.getObjectName().equalsIgnoreCase(targetName)
			&& event.getAction().equalsIgnoreCase(interactionType)
			&& event.getLocation().distanceTo(location) <= DEFAULT_LOCATION_DISTANCE;
	}
}
