package dev.hollink.pmtt.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public interface FormHelper
{

	default JPanel createRow(String labelText, JComponent field)
	{
		JPanel row = new JPanel();
		JLabel label = new JLabel(labelText);

		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		field.setAlignmentX(Component.LEFT_ALIGNMENT);

		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
		row.add(label);
		row.add(Box.createVerticalStrut(4));
		row.add(field);
		row.add(Box.createVerticalStrut(8));

		return row;
	}

	default JPanel createRow(JComponent field)
	{
		JPanel row = new JPanel();
		field.setAlignmentX(Component.LEFT_ALIGNMENT);

		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
		row.add(field);
		row.add(Box.createVerticalStrut(8));

		return row;
	}

	default JButton createButton(String labelText, ActionListener action)
	{
		JButton button = new JButton(labelText);
		button.addActionListener(action);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));

		return button;
	}
}
