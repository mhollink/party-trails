package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.builder.FormHelper;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.TrailStep;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import net.runelite.api.Skill;

public class SkillStepEditor extends StepEditor implements FormHelper
{
	private final JComboBox<Skill> skillIdField = new JComboBox<>(Skill.values());
	private final JTextField hint = new JTextField();
	private final JTextField expField = new JTextField();

	@Override
	public void initForm()
	{
		add(createRow("Hint", hint));
		add(createRow("Skill", skillIdField));
		add(createRow("Experience", expField));

		// TODO: Region selector
	}

	@Override
	boolean onCapture(ClueEvent event)
	{
		return false;
	}

	@Override
	public TrailStep toTrailStep()
	{
		return null;
	}
}
