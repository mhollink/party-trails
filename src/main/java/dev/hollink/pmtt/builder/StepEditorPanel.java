package dev.hollink.pmtt.builder;

import dev.hollink.pmtt.builder.editors.StepEditor;
import dev.hollink.pmtt.builder.editors.StepEditorFactory;
import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.runetime.EventBus;
import java.awt.Color;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StepEditorPanel extends JPanel implements FormHelper
{
	private final EventBus eventBus;

	private final JComboBox<StepType> typeSelect = new JComboBox<>(StepType.values());

	private StepEditor currentStepEditor;

	public StepEditorPanel(EventBus eventBus, Consumer<StepEditorPanel> deleteCallback, Runnable updateCallback)
	{
		this.eventBus = eventBus;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		buildEditor(deleteCallback, updateCallback);

		typeSelect.addActionListener(e -> rebuildEditor(deleteCallback, updateCallback));
	}

	public void setStepNumber(int number)
	{
		setBorder(BorderFactory.createTitledBorder("Step " + number));
	}

	private void buildEditor(Consumer<StepEditorPanel> deleteCallback, Runnable updateCallback)
	{
		add(createRow(typeSelect));

		StepType type = (StepType) typeSelect.getSelectedItem();
		currentStepEditor = StepEditorFactory.create(type);
		currentStepEditor.initForm();

		eventBus.register(currentStepEditor::onEvent);

		add(currentStepEditor);
		add(Box.createVerticalStrut(8));
		add(createButton("Delete step", (e) -> deleteCallback.accept(this)));

		revalidate();
		repaint();

		updateCallback.run();
	}

	private void rebuildEditor(Consumer<StepEditorPanel> deleteCallback, Runnable updateCallback)
	{
		if (currentStepEditor != null)
		{
			eventBus.unregister(currentStepEditor::onEvent);
		}
		removeAll();
		buildEditor(deleteCallback, updateCallback);
	}

	public TrailStep toTrailStep()
	{
		return currentStepEditor.toTrailStep();
	}
}