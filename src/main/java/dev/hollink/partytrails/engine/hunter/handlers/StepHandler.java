package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.events.events.TrailEvent;

public interface StepHandler<E extends TrailEvent>
{
	void onEvent(E event);

	default void onDeactivate()
	{
	}
}
