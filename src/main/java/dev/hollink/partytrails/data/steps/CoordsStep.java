package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.AnimationEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.trail.TrailContext;
import java.awt.Graphics2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;

@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public final class CoordsStep implements TrailStep
{

	private final String hint;
	private final WorldPoint targetLocation;

	@Override
	public StepType getStepType()
	{
		return StepType.COORDINATE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Coordinate clue", panel);
		drawText(hint, panel);
	}

	@Override
	public void onActivate(TrailContext context)
	{
		// Noop.
	}

	@Override
	public boolean handlesEvent(TrailEvent event)
	{
		return event instanceof AnimationEvent;
	}

	@Override
	public boolean isComplete(TrailContext context, TrailEvent event)
	{
		if (event instanceof AnimationEvent)
		{
			AnimationEvent animationEvent = (AnimationEvent) event;
			return animationEvent.getAnimationId() == HUMAN_DIG
				&& isInRange(targetLocation, animationEvent.getLocation());
		}
		else
		{
			return false;
		}
	}
}
