package dev.hollink.partytrails.engine.hunter.handlers;

import dev.hollink.partytrails.data.steps.CoordsStep;
import dev.hollink.partytrails.data.steps.EmoteStep;
import dev.hollink.partytrails.data.steps.InteractionStep;
import dev.hollink.partytrails.data.steps.SkillStep;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.TrailEventBus;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class StepHandlerFactory
{
	private final TrailEventBus events;

	public StepHandler<?> createHandler(TrailStep step, TrailContext context)
	{
		switch (step.getStepType())
		{
			case EMOTE_STEP:
				return new EmoteStepHandler(events, (EmoteStep) step, context);
			case COORDINATE_STEP:
				return new CoordinateStepHandler(events, (CoordsStep) step, context);
			case SKILL_STEP:
				return new SkillStepHandler(events, (SkillStep) step, context);
			case CIPHER_STEP:
			case ANAGRAM_STEP:
			case CRYPTIC_STEP:
				return new InteractionStepHandler(events, (InteractionStep) step, context);

			default:
				throw new IllegalArgumentException("Unknown step type: " + step.getStepType());
		}
	}
}