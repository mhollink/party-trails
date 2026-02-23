package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.trail.Encodable;
import dev.hollink.pmtt.data.trail.Overlayable;
import dev.hollink.pmtt.data.trail.Steppable;
import net.runelite.api.coords.WorldPoint;

public interface TrailStep extends Steppable, Overlayable, Encodable
{
	int DEFAULT_LOCATION_DISTANCE = 3;

	default boolean isInRange(WorldPoint a, WorldPoint b)
	{
		return a.distanceTo(b) <= DEFAULT_LOCATION_DISTANCE;
	}
}
