package dev.hollink.partytrails.events.events;

import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.trail.TrailContext;
import lombok.Value;

@Value
public class TrailCompletedEvent
{
	TreasureTrail trail;
	TrailContext context;
}
