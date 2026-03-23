package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.data.steps.SkillStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.Subscription;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.SkillEvent;
import dev.hollink.partytrails.events.events.TrailStepCompletedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkillStepHandler implements StepHandler<SkillEvent>
{
	private final TrailEventBus events;
	private final SkillStep step;
	private final TrailContext context;
	private final Subscription subscription;

	public SkillStepHandler(TrailEventBus events, SkillStep step, TrailContext context)
	{
		this.events = events;
		this.step = step;
		this.context = context;

		storeStartingExperience();

		this.subscription = events.register(SkillEvent.class, this::onEvent);
	}

	public void storeStartingExperience()
	{
		String key = getContextKey(context);

		log.debug("Set initial experience for {} in the skill-step state", step.getSkill().getName());
		int startExp = context.getSkillExperience(step.getSkill());
		context.getProgress().storeInt(key, startExp);
	}

	@Override
	public void onDeactivate()
	{
		subscription.unsubscribe();

		log.debug("Clearing skill step state from context.");
		context.getProgress().getStepState().clear();
	}

	@Override
	public void onEvent(SkillEvent event)
	{
		if (step.getSkill() != event.getSkill())
		{
			return;
		}

		if (!context.getClient().getLocalPlayer().getWorldLocation().isInArea(step.getArea()))
		{
			log.debug("Player performed skilling action for {} outside of target area!", step.getSkill().getName());
			return;
		}

		String key = getContextKey(context);
		int startExp = context.getProgress().getStoredInt(key);
		int expDiff = event.getXp() - startExp;

		if (expDiff < step.getExpRequired())
		{
			log.debug("Player performed skilling action for {}", step.getSkill().getName());
			return;
		}

		events.publish(new TrailStepCompletedEvent(step, context));
	}

	private String getContextKey(TrailContext context)
	{
		int stepIndex = context.getProgress().getCurrentStepIndex();
		String skillName = step.getSkill().getName().toLowerCase();
		return String.format("step.%d.%s-experience", stepIndex, skillName);
	}
}
