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
import javax.swing.JTextArea;

public class ObjectInteractionStepEditor extends StepEditor implements FormHelper
{
	private final JTextArea hintArea = new JTextArea();
	private final JTextArea objectId = new JTextArea();
	private final JTextArea objectName = new JTextArea();
	private final JTextArea action = new JTextArea();
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
	boolean onCapture(ClueEvent event)
	{
		if (event instanceof InteractionEvent interactionEvent)
		{
			objectId.setText(String.valueOf(interactionEvent.objectId()));
			objectName.setText(interactionEvent.objectName());
			action.setText(interactionEvent.action());
			locationSelector.setLocation(interactionEvent.location());
			return true;
		}
		return false;
	}

	@Override
	public TrailStep toTrailStep()
	{
		InteractionTarget target = new InteractionTarget(Integer.parseInt(objectId.getText()), objectName.getText(), action.getText(), locationSelector.getWorldLocation());
		return switch (stepType)
		{
			case CIPHER_STEP -> new CipherStep(hintArea.getText(), target);
			case CRYPTIC_STEP -> new CrypticStep(hintArea.getText(), target);
			case ANAGRAM_STEP -> new AnagramStep(hintArea.getText(), target);
			default -> throw new IllegalStateException("Unexpected value: " + stepType);
		};
	}
}
