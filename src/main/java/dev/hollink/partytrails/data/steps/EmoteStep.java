package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.Emote;
import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.AnimationEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.trail.TrailContext;
import java.awt.Color;
import java.awt.Graphics2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

@ToString
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public final class EmoteStep implements TrailStep
{

	private final String hint;
	private final Emote targetEmoteOne;
	private final WorldPoint targetLocation;

	@Override
	public StepType getStepType()
	{
		return StepType.EMOTE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Emote Clue", panel);
		drawText("Emote:", panel, Color.WHITE);
		drawText(targetEmoteOne.getName(), panel);
		drawText("Location:", panel, Color.WHITE);
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
			return animationEvent.getAnimationId() == targetEmoteOne.getAnimationId()
				&& animationEvent.getLocation().distanceTo(targetLocation) <= DEFAULT_LOCATION_DISTANCE;
		}
		else
		{
			return false;
		}
	}

}


