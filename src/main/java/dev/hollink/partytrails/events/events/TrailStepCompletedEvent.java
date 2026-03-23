package dev.hollink.partytrails.events.events;

import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import lombok.Value;

@Value
public class TrailStepCompletedEvent
{
	TrailStep step;
	TrailContext context;
}
