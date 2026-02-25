package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.LocationSelector;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.utils.SextantUtil;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.AnimationID;

public final class CoordinateStepEditor extends StepEditor implements FormHelper
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

	@Override
	public List<StepEditorValidationError> validateUserInput()
	{
		List<StepEditorValidationError> errors = new ArrayList<StepEditorValidationError>();
		WorldPoint location = locationSelector.getWorldLocation();
		if (location == null) {
			errors.add(new StepEditorValidationError(stepNumber, "Location", "You must specify a valid location"));
		} else if (location.getPlane() != 0) {
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
}
