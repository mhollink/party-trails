package dev.hollink.partytrails.data.trail;

import dev.hollink.partytrails.data.events.TrailEvent;

public interface Steppable
{

	void onActivate(TrailContext context);

	boolean handlesEvent(TrailEvent event);

	boolean isComplete(TrailContext context, TrailEvent event);

}
