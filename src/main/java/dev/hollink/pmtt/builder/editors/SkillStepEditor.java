package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.RegionSelector;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.SkillStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import net.runelite.api.Skill;
import net.runelite.api.gameval.AnimationID;

public class SkillStepEditor extends StepEditor implements FormHelper
{
	private final JComboBox<Skill> skillIdField = new JComboBox<>(Skill.values());
	private final JTextField hint = new JTextField();
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
	boolean onCapture(ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent && animationEvent.animationId() == AnimationID.HUMAN_DIG)
		{
			if (!waitingForSecondDig)
			{
				regionSelector.setLocation(animationEvent.location());
				waitingForSecondDig = true;
				captureButton.setText("Dig to complete area!");
				return false;
			}
			else
			{
				boolean hasBeenSet = regionSelector.setSize(animationEvent.location());
				if (hasBeenSet)
				{
					waitingForSecondDig = false;
					return true;
				}
			}
		}
		return false;
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
