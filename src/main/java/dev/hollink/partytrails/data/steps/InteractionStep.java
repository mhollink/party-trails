package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.InteractionTarget;
import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.InteractionEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.trail.TrailContext;
import java.awt.Graphics2D;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public final class InteractionStep implements TrailStep
{
	public static final List<StepType> ALLOWED_TYPES = List.of(StepType.ANAGRAM_STEP, StepType.CIPHER_STEP, StepType.CRYPTIC_STEP);

	private final StepType stepType;
	private final String hint;
	private final InteractionTarget target;

	public InteractionStep(StepType stepType, String hint, InteractionTarget target)
	{
		if (stepType == null || !ALLOWED_TYPES.contains(stepType))
		{
			throw new IllegalArgumentException("Invalid step type: " + stepType);
		}

		this.stepType = stepType;
		this.hint = hint;
		this.target = target;
	}

	public StepType getStepType()
	{
		return stepType;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle(stepType.stepTypeName, panel);
		drawText(hint, panel);
	}


	@Override
	public void onActivate(TrailContext context)
	{
		// noop
	}

	@Override
	public boolean handlesEvent(TrailEvent event)
	{
		return event instanceof InteractionEvent;
	}

	@Override
	public boolean isComplete(TrailContext context, TrailEvent event)
	{
		if (event instanceof InteractionEvent)
		{
			InteractionEvent interactionEvent = (InteractionEvent) event;
			return target.checkEvent(interactionEvent);
		}
		else
		{
			return false;
		}
	}


}
