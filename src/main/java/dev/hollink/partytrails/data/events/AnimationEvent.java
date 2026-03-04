package dev.hollink.partytrails.data.events;

import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@Value
public class AnimationEvent implements TrailEvent
{
	int animationId;
	WorldPoint location;
}
