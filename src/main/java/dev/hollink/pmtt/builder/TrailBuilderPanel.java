package dev.hollink.pmtt.builder;

import dev.hollink.pmtt.builder.editors.StepEditorValidationError;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.encoding.TrailEncoder;
import dev.hollink.pmtt.runetime.TrailEventBus;
import dev.hollink.pmtt.utils.RandomUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import net.runelite.api.Client;
import net.runelite.client.ui.PluginPanel;

public final class TrailBuilderPanel extends PluginPanel implements FormHelper
{
	private final Client client;
	private final TrailEventBus clueEventBus;

	private final JTextField nameField = new JTextField();

	private final JPanel stepsContainer = new JPanel();
	private final List<StepEditorPanel> editors = new ArrayList<>();

	public TrailBuilderPanel(Client client, TrailEventBus clueEventBus)
	{
		this.client = client;
		this.clueEventBus = clueEventBus;

		setLayout(new BorderLayout());

		JPanel form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

		addMetadataSection(form);
		addStepsSection(form);
		addButtons(form);

		// Start with a single step already present!
		addStep();

		JScrollPane scrollPane = new JScrollPane(form);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}


	private void addMetadataSection(JPanel form)
	{
		form.add(createRow("Trail name", nameField));
	}

	private void addStepsSection(JPanel form)
	{
		JButton addStep = new JButton("Add Step");
		addStep.addActionListener(e -> addStep());
		addStep.setMaximumSize(new Dimension(Integer.MAX_VALUE, addStep.getPreferredSize().height));

		stepsContainer.setLayout(new BoxLayout(stepsContainer, BoxLayout.Y_AXIS));
		form.add(stepsContainer);
		form.add(Box.createVerticalStrut(8));
		form.add(addStep);
	}

	private void addStep()
	{
		StepEditorPanel panel = new StepEditorPanel(this.clueEventBus, this::removeStep, this::updateUI);
		editors.add(panel);
		rebuildSteps();
	}

	private void removeStep(StepEditorPanel panel)
	{
		editors.remove(panel);
		rebuildSteps();
	}

	private void rebuildSteps()
	{
		stepsContainer.removeAll();

		for (int i = 0; i < editors.size(); i++)
		{
			StepEditorPanel editor = editors.get(i);
			editor.setStepNumber(i + 1);
			stepsContainer.add(editor);
			stepsContainer.add(Box.createVerticalStrut(8));
		}

		stepsContainer.revalidate();
		stepsContainer.repaint();
		updateUI();
	}

	private void addButtons(JPanel root)
	{
		root.add(Box.createVerticalStrut(8));

		JButton encode = new JButton("Encode & Copy");
		encode.setMaximumSize(new Dimension(Integer.MAX_VALUE, encode.getPreferredSize().height));
		encode.addActionListener(e -> encodeTrail());

		root.add(encode);
	}

	private void encodeTrail()
	{
		if (client.getLocalPlayer() == null)
		{
			JOptionPane.showMessageDialog(this, "You must be logged in to encode a trail", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (nameField.getText().isBlank())
		{
			JOptionPane.showMessageDialog(this, "Trail name required", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (editors.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Add at least one step", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Set<StepEditorValidationError> errors = editors.stream()
			.flatMap(editor -> editor.getValidationErrors().stream())
			.collect(Collectors.toUnmodifiableSet());

		if (!errors.isEmpty())
		{
			String message = errors.stream()
				.sorted(Comparator.comparingInt(StepEditorValidationError::getStepIndex))
				.map(error -> String.format("Step %d: %s", error.getStepIndex(), error.getErrorMessage()))
				.collect(Collectors.joining("\n"));
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<TrailStep> steps = editors.stream()
			.map(StepEditorPanel::toTrailStep)
			.collect(Collectors.toList());

		TreasureTrail trail = new TreasureTrail(
			1,
			RandomUtil.randomAlphanumeric(16),
			nameField.getText(),
			client.getLocalPlayer().getName(),
			steps
		);

		try
		{
			String encodeTrail = TrailEncoder.encodeTrail(trail);
			Toolkit.getDefaultToolkit()
				.getSystemClipboard()
				.setContents(new StringSelection(encodeTrail), null);
			JOptionPane.showMessageDialog(this, "Trail copied to clipboard");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}


}