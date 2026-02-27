package dev.hollink.pmtt.data.events;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class AnimationEvent implements ClueEvent
{
	int animationId;
	WorldPoint location;
}
