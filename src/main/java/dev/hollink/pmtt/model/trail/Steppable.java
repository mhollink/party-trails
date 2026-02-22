package dev.hollink.pmtt.model.trail;

import dev.hollink.pmtt.model.events.ClueEvent;

public interface Steppable
{

	void onActivate(ClueContext context);

	boolean isComplete(ClueEvent event);
}
