package dev.hollink.pmtt.data;

import dev.hollink.pmtt.data.events.InteractionEvent;
import static dev.hollink.pmtt.data.steps.TrailStep.DEFAULT_LOCATION_DISTANCE;
import net.runelite.api.coords.WorldPoint;

public record InteractionTarget(int targetId, String targetName, String interactionType, WorldPoint location)
{
	public boolean checkEvent(InteractionEvent event)
	{
		return event.objectId() == targetId
			&& event.objectName().equals(targetName)
			&& event.action().equals(interactionType)
			&& event.location().distanceTo(location) <= DEFAULT_LOCATION_DISTANCE;
	}
}
