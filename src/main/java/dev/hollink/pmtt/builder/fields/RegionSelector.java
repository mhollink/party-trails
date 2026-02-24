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
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public final class RegionSelector extends JPanel
{
	private final JTextField xField = new JTextField();
	private final JTextField yField = new JTextField();
	private final JTextField widthField = new JTextField();
	private final JTextField heightField = new JTextField();
	private final JTextField planeField = new JTextField("0");

	public RegionSelector()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel xyPanel = new JPanel();
		xyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		xyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		xyPanel.add(createRow("X", xField));
		xyPanel.add(Box.createVerticalStrut(5));
		xyPanel.add(createRow("Y", yField));
		xyPanel.add(Box.createVerticalStrut(5));
		xyPanel.add(createRow("Plane", planeField));

		JPanel whPanel = new JPanel();
		whPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		whPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		whPanel.add(createRow("Width", widthField));
		whPanel.add(Box.createVerticalStrut(5));
		whPanel.add(createRow("Height", heightField));

		setupFields();

		add(xyPanel);
		add(whPanel);
		add(Box.createVerticalStrut(8));
	}

	private void setupFields()
	{
		xField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		yField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		widthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		heightField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
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

	private WorldPoint getWorldLocation()
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

	public WorldArea getWorldArea()
	{
		try
		{
			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());
			int w = Integer.parseInt(widthField.getText());
			int h = Integer.parseInt(heightField.getText());
			int plane = Integer.parseInt(planeField.getText());

			return new WorldArea(x, y, w, h, plane);
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

	public boolean setSize(WorldPoint point)
	{
		WorldPoint initialLocation = getWorldLocation();
		if (initialLocation == null)
		{
			setLocation(point);
			return false;
		}
		if (initialLocation.getPlane() != point.getPlane())
		{
			return false;
		}
		int xDiff = initialLocation.getX() - point.getX();
		int yDiff = initialLocation.getY() - point.getY();

		if (xDiff == 0 || yDiff == 0)
		{
			return false;
		}

		if (xDiff < 0)
		{
			xField.setText(String.valueOf(point.getX()));
			widthField.setText(String.valueOf(Math.abs(xDiff)));
		}
		else
		{
			widthField.setText(String.valueOf(xDiff));
		}
		if (yDiff < 0)
		{
			xField.setText(String.valueOf(point.getY()));
			heightField.setText(String.valueOf(Math.abs(yDiff)));
		}
		else
		{
			heightField.setText(String.valueOf(yDiff));
		}

		return true;
	}
}