package dev.hollink.pmtt.data.trail;

import dev.hollink.pmtt.data.events.ClueEvent;

public interface Steppable
{

	void onActivate(ClueContext context);

	boolean isComplete(ClueEvent event);
}
