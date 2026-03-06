package dev.hollink.partytrails.builder.editors;

import dev.hollink.partytrails.builder.FormHelper;
import dev.hollink.partytrails.builder.fields.LocationSelector;
import dev.hollink.partytrails.data.Emote;
import dev.hollink.partytrails.data.events.AnimationEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.steps.EmoteStep;
import dev.hollink.partytrails.data.steps.TrailStep;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

@Slf4j
public final class EmoteStepEditor extends StepEditor implements FormHelper
{
	private final JComboBox<Emote> emoteIdField = new JComboBox<>(Emote.values());
	private final JTextArea hintArea = createTextArea();
	private final LocationSelector locationSelector = new LocationSelector();

	@Override
	public void initForm()
	{
		add(createRow("Hint", hintArea));
		add(createRow("Emote", emoteIdField));
		add(locationSelector);
		add(Box.createVerticalStrut(8));
		add(captureButton);
	}

	@Override
	protected boolean onCapture(TrailEvent event)
	{
		if (event instanceof AnimationEvent)
		{
			AnimationEvent animationEvent = (AnimationEvent) event;
			int animationId = animationEvent.getAnimationId();
			Emote emote = Emote.fromAnimationId(animationId);
			if (emote != null)
			{
				emoteIdField.setSelectedItem(emote);
				locationSelector.setLocation(animationEvent.getLocation());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<StepEditorValidationError> validateUserInput()
	{
		List<StepEditorValidationError> errors = new ArrayList<>();
		if (hintArea.getText().isBlank())
		{
			errors.add(new StepEditorValidationError(stepNumber, "Hint", "A hint is required"));
		}
		WorldPoint location = locationSelector.getWorldLocation();
		if (location == null)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "You must specify a valid location"));
		}
		return errors;
	}

	@Override
	public TrailStep toTrailStep()
	{
		return new EmoteStep(
			hintArea.getText(),
			(Emote) emoteIdField.getSelectedItem(),
			locationSelector.getWorldLocation()
		);
	}

	@Override
	protected void updateButtonText()
	{
		captureButton.setText("Perform emote to set values!");
	}

	@Override
	public void setTrailStep(TrailStep trailStep)
	{
		if (trailStep instanceof EmoteStep)
		{
			EmoteStep emoteStep = (EmoteStep) trailStep;
			emoteIdField.setSelectedItem(emoteStep.getTargetEmoteOne());
			hintArea.setText(emoteStep.getHint());
			locationSelector.setLocation(emoteStep.getTargetLocation());
			log.debug("reloaded emote step {}", stepNumber);
		}
		else
		{
			log.debug("Unable to set emote step values, given step was of type {}", trailStep.type());
		}
	}
}
