package dev.hollink.partytrails.builder.editors;

import dev.hollink.partytrails.builder.FormHelper;
import dev.hollink.partytrails.builder.fields.LocationSelector;
import dev.hollink.partytrails.data.events.AnimationEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.steps.CoordsStep;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.utils.SextantUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.AnimationID;

@Slf4j
public final class CoordinateStepEditor extends StepEditor implements FormHelper
{
	private final LocationSelector locationSelector = new LocationSelector();

	public void initForm()
	{
		add(locationSelector);
		add(captureButton);
	}

	@Override
	public boolean onCapture(TrailEvent event)
	{
		if (event instanceof AnimationEvent)
		{
			AnimationEvent animationEvent = (AnimationEvent) event;
			if (animationEvent.getLocation().getPlane() == 0 &&
				animationEvent.getAnimationId() == AnimationID.HUMAN_DIG)
			{
				locationSelector.setLocation(animationEvent.getLocation());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<StepEditorValidationError> validateUserInput()
	{
		List<StepEditorValidationError> errors = new ArrayList<StepEditorValidationError>();
		WorldPoint location = locationSelector.getWorldLocation();
		if (location == null)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "You must specify a valid location"));
		}
		else if (location.getPlane() != 0)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "The coordinate clue can only be on the Gielinor Surface"));
		}
		return errors;
	}

	@Override
	public TrailStep toTrailStep()
	{
		WorldPoint location = locationSelector.getWorldLocation();
		if (location != null)
		{
			String hint = SextantUtil.getCoordinates(location);
			return new CoordsStep(hint, location);
		}
		else
		{
			return null;
		}
	}

	@Override
	protected void updateButtonText()
	{
		captureButton.setText("Dig to set location!");
	}

	@Override
	public void setTrailStep(TrailStep trailStep)
	{
		if(trailStep instanceof CoordsStep) {
			CoordsStep coordsStep = (CoordsStep) trailStep;
			locationSelector.setLocation(coordsStep.getTargetLocation());
			log.debug("reloaded coordinate step {}", stepNumber);
			log.debug(coordsStep.toString());
		}else {
			log.warn("Unable to set coordinate step values, given step was of type {}", trailStep.type());
		}
	}
}
