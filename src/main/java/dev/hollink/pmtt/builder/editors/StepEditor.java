package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.TrailStep;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class StepEditor extends JPanel
{

	protected final JButton captureButton;
	protected boolean captureMode = false;
	@Setter
	protected int stepNumber = 0;

	public StepEditor()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));

		captureButton = new JButton("Capture in game");
		JToolTip toolTip = captureButton.createToolTip();
		toolTip.setTipText("Perform the clue action in game to capture the input values");
		captureButton.addActionListener(e -> {
			captureMode = true;
			updateButtonText();
		});
		captureButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		captureButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, captureButton.getPreferredSize().height));
	}

	public abstract void initForm();

	public void onEvent(ClueEvent event)
	{
		if (captureMode)
		{
			if (this.onCapture(event))
			{
				resetCaptureMode();
			}
		}
	}

	protected abstract boolean onCapture(ClueEvent event);

	protected void updateButtonText()
	{
		captureButton.setText("Perform action in game...");
	}

	protected void resetCaptureMode()
	{
		captureMode = false;
		captureButton.setText("Capture in game");
	}

	public abstract List<StepEditorValidationError> validateUserInput();

	public abstract TrailStep toTrailStep();
}
