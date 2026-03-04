package dev.hollink.partytrails.builder.editors;

import dev.hollink.partytrails.builder.FormHelper;
import dev.hollink.partytrails.builder.fields.LocationSelector;
import dev.hollink.partytrails.data.InteractionTarget;
import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.events.InteractionEvent;
import dev.hollink.partytrails.data.steps.InteractionStep;
import dev.hollink.partytrails.data.steps.TrailStep;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

@Slf4j
public final class InteractionStepEditor extends StepEditor implements FormHelper
{
	private final JTextArea hintArea = createTextArea();
	private final JTextField objectId = new JTextField();
	private final JTextField objectName = new JTextField();
	private final JTextField action = new JTextField();
	private final LocationSelector locationSelector = new LocationSelector();

	private final StepType stepType;

	public InteractionStepEditor(StepType stepType)
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
	protected boolean onCapture(TrailEvent event)
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
		return new InteractionStep(stepType, hintArea.getText(), target);
	}

	@Override
	public void setTrailStep(TrailStep trailStep)
	{
		if(trailStep instanceof InteractionStep) {
			InteractionStep interactionStep = (InteractionStep) trailStep;
			InteractionTarget target = interactionStep.getTarget();
			hintArea.setText(interactionStep.getHint());
			objectId.setText(String.valueOf(target.getTargetId()));
			objectName.setText(String.valueOf(target.getTargetName()));
			action.setText(String.valueOf(target.getInteractionType()));
			locationSelector.setLocation(target.getLocation());
			log.debug("reloaded interaction step {}", stepNumber);
			log.debug(interactionStep.toString());
		} else {
			log.warn("Unable to set interaction step values, given step was of type {}", trailStep.type());
		}
	}
}
