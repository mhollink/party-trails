package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.data.steps.EmoteStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.Subscription;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.AnimationEvent;
import dev.hollink.partytrails.events.events.TrailStepCompletedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmoteStepHandler implements StepHandler<AnimationEvent>
{
	private final TrailEventBus events;
	private final EmoteStep step;
	private final TrailContext context;
	private final Subscription subscription;

	public EmoteStepHandler(TrailEventBus events, EmoteStep step, TrailContext context)
	{
		this.events = events;
		this.step = step;
		this.context = context;

		this.subscription = events.register(AnimationEvent.class, this::onEvent);
	}

	@Override
	public void onEvent(AnimationEvent event)
	{
		if (event.getAnimationId() != step.getTargetEmoteOne().getAnimationId())
		{
			return;
		}
		int distanceToTarget = event.getLocation().distanceTo(step.getTargetLocation());
		log.debug("Player performed {} at {} (distance={})", step.getTargetEmoteOne(), event.getLocation(), distanceToTarget);

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
