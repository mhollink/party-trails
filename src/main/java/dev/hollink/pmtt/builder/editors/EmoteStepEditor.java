package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.LocationSelector;
import dev.hollink.pmtt.data.Emote;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.EmoteStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

@Slf4j
public final class EmoteStepEditor extends StepEditor implements FormHelper
{
	private final JComboBox<Emote> emoteIdField = new JComboBox<>(Emote.values());
	private final JTextArea hintArea = new JTextArea(3, 0);
	private final LocationSelector locationSelector = new LocationSelector();

	@Override
	public void initForm()
	{
		add(createRow("Hint", hintArea));
		add(createRow("Emote", emoteIdField));
		add(locationSelector);
		add(captureButton);
	}

	@Override
	protected boolean onCapture(ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent)
		{
			int animationId = animationEvent.animationId();
			Emote emote = Emote.fromAnimationId(animationId);
			if (emote != null)
			{
				emoteIdField.setSelectedItem(emote);
				locationSelector.setLocation(animationEvent.location());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<StepEditorValidationError> validateUserInput()
	{
		List<StepEditorValidationError> errors = new ArrayList<>();
		if (hintArea.getText().isBlank()) {
			errors.add(new StepEditorValidationError(stepNumber, "Hint", "A hint is required"));
		}
		WorldPoint location = locationSelector.getWorldLocation();
		if (location == null) {
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
}
