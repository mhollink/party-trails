package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.builder.fields.LocationSelector;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.events.InteractionEvent;
import dev.hollink.pmtt.data.steps.AnagramStep;
import dev.hollink.pmtt.data.steps.CipherStep;
import dev.hollink.pmtt.data.steps.CrypticStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.runelite.api.coords.WorldPoint;

public final class ObjectInteractionStepEditor extends StepEditor implements FormHelper
{
	private final JTextArea hintArea = new JTextArea(3, 0);
	private final JTextField objectId = new JTextField();
	private final JTextField objectName = new JTextField();
	private final JTextField action = new JTextField();
	private final LocationSelector locationSelector = new LocationSelector();

	private final StepType stepType;

	public ObjectInteractionStepEditor(StepType stepType)
	{
		super();
		this.stepType = stepType;
	}

	@Override
	public void initForm()
	{
		add(createRow("Hint", hintArea));
		add(createRow("Object ID", objectId));
		add(createRow("Object Name", objectName));
		add(createRow("Menu entry", action));
		add(locationSelector);
		add(captureButton);
	}

	@Override
	protected boolean onCapture(ClueEvent event)
	{
		if (event instanceof InteractionEvent)
		{
			InteractionEvent interactionEvent = (InteractionEvent) event;
			objectId.setText(String.valueOf(interactionEvent.getObjectId()));
			objectName.setText(interactionEvent.getObjectName());
			action.setText(interactionEvent.getAction());
			locationSelector.setLocation(interactionEvent.getLocation());
			return true;
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
		if (hasNotCompletedTarget())
		{
			errors.add(new StepEditorValidationError(stepNumber, "Target", "Object ID, name and menu entry are required"));
		}
		else if (!hasValidTargetId())
		{
			errors.add(new StepEditorValidationError(stepNumber, "Target", "Object ID must be a number!"));
		}

		WorldPoint location = locationSelector.getWorldLocation();
		if (location == null)
		{
			errors.add(new StepEditorValidationError(stepNumber, "Location", "Target location is incorrectly formatted"));
		}
		return errors;
	}

	private boolean hasNotCompletedTarget()
	{
		return Stream.of(objectId.getText(), objectName.getText(), action.getText()).anyMatch(String::isBlank);
	}

	private boolean hasValidTargetId()
	{
		try
		{
			Integer.parseInt(objectId.getText());
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Override
	public TrailStep toTrailStep()
	{
		InteractionTarget target = new InteractionTarget(Integer.parseInt(objectId.getText()), objectName.getText(), action.getText(), locationSelector.getWorldLocation());
		switch (stepType)
		{
			case CIPHER_STEP:
				return new CipherStep(hintArea.getText(), target);
			case CRYPTIC_STEP:
				return new CrypticStep(hintArea.getText(), target);
			case ANAGRAM_STEP:
				return new AnagramStep(hintArea.getText(), target);
			default:
				throw new IllegalStateException("Unexpected value: " + stepType);
		}
	}
}
