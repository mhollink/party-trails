package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.trail.Overlayable;
import dev.hollink.partytrails.data.trail.Steppable;
import net.runelite.api.coords.WorldPoint;

public interface TrailStep extends Steppable, Overlayable
{
	int DEFAULT_LOCATION_DISTANCE = 2;

	StepType getStepType();

	default boolean isInRange(WorldPoint a, WorldPoint b)
	{
		return a.distanceTo(b) <= DEFAULT_LOCATION_DISTANCE;
	}
}
