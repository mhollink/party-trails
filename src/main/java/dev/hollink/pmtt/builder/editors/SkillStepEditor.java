package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.RegionSelector;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.SkillStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.gameval.AnimationID;

public final class SkillStepEditor extends StepEditor implements FormHelper
{
	private final JComboBox<Skill> skillIdField = new JComboBox<>(Skill.values());
	private final JTextArea hint = new JTextArea(3, 0);
	private final JTextField expField = new JTextField();
	private final RegionSelector regionSelector = new RegionSelector();

	private boolean waitingForSecondDig = false;

	@Override
	public void initForm()
	{
		add(createRow("Hint", hint));
		add(createRow("Skill", skillIdField));
		add(createRow("Experience", expField));
		add(regionSelector);
		add(captureButton);
		captureButton.setText("Capture region in game");
	}

	@Override
	protected boolean onCapture(ClueEvent event)
	{
		if (event instanceof AnimationEvent)
		{
			AnimationEvent animationEvent = (AnimationEvent) event;
			if (animationEvent.getAnimationId() == AnimationID.HUMAN_DIG)
			{
				if (!waitingForSecondDig)
				{
					regionSelector.setLocation(animationEvent.getLocation());
					waitingForSecondDig = true;
					captureButton.setText("Dig to complete area!");
					return false;
				}
				else
				{
					boolean hasBeenSet = regionSelector.setSize(animationEvent.getLocation());
					if (hasBeenSet)
					{
						waitingForSecondDig = false;
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<StepEditorValidationError> validateUserInput()
	{
		List<StepEditorValidationError> errors = new ArrayList<>();
		if (hint.getText().isBlank())
		{
			errors.add(new StepEditorValidationError(stepNumber, "Hint", "A hint is required"));
		}
		if (!hasValidExperience())
		{
			errors.add(new StepEditorValidationError(stepNumber, "Experience", "Experience must be a positive number"));
		}
		WorldArea region = regionSelector.getWorldArea();
		if (region == null)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "Target location is incorrectly formatted"));
		}
		else if (region.getWidth() <= 0 || region.getHeight() <= 0)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "Width and height must be greater than zero"));
		}
		return errors;
	}

	private boolean hasValidExperience()
	{
		try
		{
			return Integer.parseInt(expField.getText()) > 0;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Override
	public TrailStep toTrailStep()
	{
		return new SkillStep(
			hint.getText(),
			(Skill) skillIdField.getSelectedItem(),
			Integer.parseInt(expField.getText()),
			regionSelector.getWorldArea()
		);
	}

	@Override
	protected void updateButtonText()
	{
		if (!waitingForSecondDig)
		{
			captureButton.setText("Dig to set first point!");
		}
		else
		{
			captureButton.setText("Dig to complete area!");
		}
	}

	@Override
	protected void resetCaptureMode()
	{
		captureMode = false;
		captureButton.setText("Capture region in game");
	}
}
