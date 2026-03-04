package dev.hollink.partytrails.builder;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import dev.hollink.partytrails.PartyTrailsConfig;
import dev.hollink.partytrails.builder.editors.StepEditorValidationError;
import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.encoding.TrailDecoder;
import dev.hollink.partytrails.encoding.TrailEncoder;
import dev.hollink.partytrails.runetime.TrailEventBus;
import dev.hollink.partytrails.utils.RandomUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
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
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;

public final class TrailBuilderPanel extends PluginPanel implements FormHelper
{
	private final Client client;
	private final ConfigManager config;
	private final TrailEventBus clueEventBus;

	private final JTextField nameField = new JTextField();

	private final JPanel stepsContainer = new JPanel();
	private final List<StepEditorPanel> editors = new ArrayList<>();

	private final JScrollPane scrollPane;

	public TrailBuilderPanel(Client client, ConfigManager config, TrailEventBus clueEventBus)
	{
		this.client = client;
		this.config = config;
		this.clueEventBus = clueEventBus;

		setLayout(new BorderLayout());
		scrollPane = createForm();

		// start disabled.
		disableTrailBuilder();
	}

	public void enableTrailBuilder()
	{
		removeAll();

		add(scrollPane, BorderLayout.CENTER);
	}

	public void disableTrailBuilder()
	{
		removeAll();

		PluginErrorPanel pluginErrorPanel = new PluginErrorPanel();
		pluginErrorPanel.setContent("Party Trails", "Sign in to start the builder.");
		add(pluginErrorPanel);
	}

	private void addMetadataSection(JPanel form)
	{
		nameField.setMaximumSize(new Dimension(230, nameField.getPreferredSize().height));

		form.add(createRow("Trail name", nameField));
	}

	private void addStepsSection(JPanel form)
	{
		JButton addStep = new JButton("Add Step");
		addStep.addActionListener(e -> addStep());
		addStep.setMaximumSize(new Dimension(230, addStep.getPreferredSize().height));

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
		JButton encode = new JButton("Encode & Copy");
		encode.setMaximumSize(new Dimension(230, encode.getPreferredSize().height));
		encode.addActionListener(e -> encodeTrail(this::copyTrailToClipboard));

		JButton save = getLoadSaveButton("Save", e -> encodeTrail(this::saveTrailToConfig));
		JButton load = getLoadSaveButton("Load", e -> reloadSave());

		root.add(Box.createVerticalStrut(16));
		root.add(encode);
		root.add(Box.createVerticalStrut(8));
		root.add(save);
		root.add(Box.createVerticalStrut(8));
		root.add(load);
	}

	private JButton getLoadSaveButton(String text, ActionListener actionListener)
	{
		JButton button = new JButton(text);
		button.setMaximumSize(new Dimension(230, button.getPreferredSize().height));
		button.addActionListener(actionListener);
		FlatButtonBorder border = new FlatButtonBorder();
		border.applyStyleProperty("borderColor", new Color(0, 0, 0, 0.05f));
		button.setBackground(new Color(0, 0, 0, 0.05f));
		button.setBorder(border);
		return button;
	}

	private void encodeTrail(Consumer<String> callback)
	{
		if (showValidationErrors())
		{
			return;
		}

		try
		{
			TreasureTrail trail = generateTrailFromBuilderInput();
			String encodeTrail = TrailEncoder.encodeTrail(trail);
			callback.accept(encodeTrail);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void copyTrailToClipboard(String encodeTrail)
	{
		Toolkit.getDefaultToolkit()
			.getSystemClipboard()
			.setContents(new StringSelection(encodeTrail), null);
		JOptionPane.showMessageDialog(this, "Trail copied to clipboard!");
	}

	private void saveTrailToConfig(String encodeTrail)
	{
		config.setConfiguration(PartyTrailsConfig.CONFIG_GROUP, PartyTrailsConfig.BUILDER_STATE, encodeTrail);
		JOptionPane.showMessageDialog(this, "Trail saved!");
	}

	private TreasureTrail generateTrailFromBuilderInput()
	{
		List<TrailStep> steps = editors.stream()
			.map(StepEditorPanel::toTrailStep)
			.collect(Collectors.toList());

		return new TreasureTrail(
			1,
			RandomUtil.randomAlphanumeric(16),
			nameField.getText(),
			client.getLocalPlayer().getName(),
			steps
		);
	}

	private boolean showValidationErrors()
	{
		if (client.getLocalPlayer() == null)
		{
			JOptionPane.showMessageDialog(this, "You must be logged in to encode a trail", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}

		if (nameField.getText().isBlank())
		{
			JOptionPane.showMessageDialog(this, "Trail name required", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}

		if (editors.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Add at least one step", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}

		String errors = getStepErrors();
		if (!errors.isBlank())
		{
			JOptionPane.showMessageDialog(this, errors, "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}

		return false;
	}

	private String getStepErrors()
	{
		return editors.stream()
			.flatMap(editor -> editor.getValidationErrors().stream())
			.sorted(Comparator.comparingInt(StepEditorValidationError::getStepIndex))
			.map(error -> String.format("Step %d: %s", error.getStepIndex(), error.getErrorMessage()))
			.collect(Collectors.joining("\n"));
	}

	private void reloadSave()
	{
		TreasureTrail treasureTrail = getSavedTrailFromConfig();
		if (treasureTrail == null)
		{
			return;
		}

		nameField.setText(treasureTrail.getMetadata().getTrailName());
		editors.clear();
		for (int i = 0; i < treasureTrail.getSteps().size(); i++)
		{
			StepEditorPanel step = new StepEditorPanel(clueEventBus, this::removeStep, this::updateUI);
			step.reloadTrailStep(i, treasureTrail.getSteps().get(i));
			editors.add(step);
		}
		rebuildSteps();
		JOptionPane.showMessageDialog(this, "Successfully loaded save for:\n" + treasureTrail.getMetadata().getTrailName());
	}

	private TreasureTrail getSavedTrailFromConfig()
	{
		String configuredSave = config.getConfiguration(PartyTrailsConfig.CONFIG_GROUP, PartyTrailsConfig.BUILDER_STATE);
		if (configuredSave == null || configuredSave.isBlank())
		{
			JOptionPane.showMessageDialog(this, "No save found in plugin configuration",
				"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		TreasureTrail treasureTrail = null;
		try
		{
			treasureTrail = TrailDecoder.decodeTrail(configuredSave);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Problem encountered while decoding progress: " + e.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		String playerName = client.getLocalPlayer().getName();
		String trailAuthor = treasureTrail.getMetadata().getAuthor();
		if (!trailAuthor.equals(playerName))
		{
			JOptionPane.showMessageDialog(this, "You can only reload trails that belong to your account!",
				"Warning", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return treasureTrail;
	}

	private JScrollPane createForm()
	{
		JPanel form = new JPanel();
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

		addMetadataSection(form);
		addStepsSection(form);
		addButtons(form);

		// Start with a single step already present!
		addStep();

		JScrollPane scrollPane = new JScrollPane(form);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		return scrollPane;
	}
}