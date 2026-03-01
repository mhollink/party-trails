package dev.hollink.pmtt.data.trail;

import dev.hollink.pmtt.data.events.ClueEvent;

public interface Steppable
{

	void onActivate(TrailContext context);

	boolean handlesEvent(ClueEvent event);

	boolean isComplete(TrailContext context, ClueEvent event);

}
