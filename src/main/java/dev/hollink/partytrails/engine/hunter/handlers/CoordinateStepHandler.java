package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.data.steps.CoordsStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.Subscription;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.AnimationEvent;
import dev.hollink.partytrails.events.events.TrailStepCompletedEvent;
import lombok.extern.slf4j.Slf4j;

import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;

@Slf4j
public class CoordinateStepHandler implements StepHandler<AnimationEvent>
{
	private final TrailEventBus events;
	private final CoordsStep step;
	private final TrailContext context;
	private final Subscription subscription;

	public CoordinateStepHandler(TrailEventBus events, CoordsStep step, TrailContext context)
	{
		this.events = events;
		this.step = step;
		this.context = context;

		this.subscription = events.register(AnimationEvent.class, this::onEvent);
	}

	@Override
	public void onEvent(AnimationEvent event)
	{
		if (event.getAnimationId() != HUMAN_DIG)
		{
			return;
		}
		int distanceToTarget = event.getLocation().distanceTo(step.getTargetLocation());
		log.debug("Player dug at {} (distance={})", event.getLocation(), distanceToTarget);

		if (distanceToTarget > 2)
		{
			return;
		}

		events.publish(new TrailStepCompletedEvent(step, context));
	}

	@Override
	public void onDeactivate()
	{
		subscription.unsubscribe();
	}
}
