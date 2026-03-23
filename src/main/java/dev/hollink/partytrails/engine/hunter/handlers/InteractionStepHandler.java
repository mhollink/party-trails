package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.data.steps.InteractionStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.Subscription;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.InteractionEvent;
import dev.hollink.partytrails.events.events.TrailStepCompletedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InteractionStepHandler implements StepHandler<InteractionEvent>
{
	private final TrailEventBus events;
	private final InteractionStep step;
	private final TrailContext context;
	private final Subscription subscription;

	public InteractionStepHandler(TrailEventBus events, InteractionStep step, TrailContext context)
	{
		this.events = events;
		this.step = step;
		this.context = context;

		this.subscription = events.register(InteractionEvent.class, this::onEvent);
	}

	@Override
	public void onEvent(InteractionEvent event)
	{
		boolean isMet = event.getObjectId() == step.getTarget().getTargetId()
			&& event.getObjectName().equalsIgnoreCase(step.getTarget().getTargetName())
			&& event.getAction().equalsIgnoreCase(step.getTarget().getInteractionType())
			&& event.getLocation().distanceTo(step.getTarget().getLocation()) <= 2;

		if (isMet)
		{
			log.debug("Player interacted with target: {}", step.getTarget().getTargetName());
			events.publish(new TrailStepCompletedEvent(step, context));
		}
	}

	@Override
	public void onDeactivate()
	{
		subscription.unsubscribe();
	}
}
