package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.LocationSelector;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.AnimationID;

public class CoordinateStepEditor extends StepEditor implements FormHelper
{
	private final LocationSelector locationSelector = new LocationSelector();

	public void initForm()
	{
		add(locationSelector);
		add(captureButton);
	}

	@Override
	public boolean onCapture(ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent)
		{
			if (animationEvent.location().getPlane() == 0 &&
				animationEvent.animationId() == AnimationID.HUMAN_DIG)
			{
				locationSelector.setLocation(animationEvent.location());
				return true;
			}
		}
		return false;
	}

	private String generateHint(WorldPoint worldPoint)
	{
		// TODO: Generate coordinate clue hint
		return "";
	}

	@Override
	public TrailStep toTrailStep()
	{
		WorldPoint location = locationSelector.getWorldLocation();
		String hint = generateHint(location);
		return new CoordsStep(hint, location);
	}
}
