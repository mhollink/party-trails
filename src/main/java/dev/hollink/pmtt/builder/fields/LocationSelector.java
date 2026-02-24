package dev.hollink.pmtt.builder.fields;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.runelite.api.coords.WorldPoint;

public final class LocationSelector extends JPanel
{
	private final JTextField xField = new JTextField();
	private final JTextField yField = new JTextField();
	private final JTextField planeField = new JTextField("0");

	public LocationSelector()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputPanel.add(createRow("X", xField));
		inputPanel.add(Box.createVerticalStrut(5));
		inputPanel.add(createRow("Y", yField));
		inputPanel.add(Box.createVerticalStrut(5));
		inputPanel.add(createRow("Plane", planeField));

		setupFields();

		add(inputPanel);
		add(Box.createVerticalStrut(8));
	}

	private void setupFields()
	{
		xField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		yField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		planeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
	}

	private JPanel createRow(String labelText, JComponent field)
	{
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
		row.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel label = new JLabel(labelText);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);

		field.setAlignmentX(Component.LEFT_ALIGNMENT);

		row.add(label);
		row.add(field);

		return row;
	}

	public WorldPoint getWorldLocation()
	{
		try
		{
			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());
			int plane = Integer.parseInt(planeField.getText());

			return new WorldPoint(x, y, plane);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void setLocation(WorldPoint point)
	{
		if (point == null)
		{
			return;
		}

		xField.setText(String.valueOf(point.getX()));
		yField.setText(String.valueOf(point.getY()));
		planeField.setText(String.valueOf(point.getPlane()));
	}
}