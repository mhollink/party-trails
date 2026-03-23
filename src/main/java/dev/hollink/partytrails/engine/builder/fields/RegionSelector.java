package dev.hollink.partytrails.engine.builder.fields;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public final class RegionSelector extends LocationSelector
{
	private final JTextField widthField = new JTextField();
	private final JTextField heightField = new JTextField();

	public RegionSelector()
	{
		super();
		JPanel whPanel = new JPanel();
		whPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		whPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		whPanel.add(createRow("Width", widthField));
		whPanel.add(Box.createVerticalStrut(5));
		whPanel.add(createRow("Height", heightField));

		setupFields();

		add(whPanel);
	}

	private void setupFields()
	{
		widthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		heightField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
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

		widthField.setText(String.valueOf(Math.abs(xDiff)));
		heightField.setText(String.valueOf(Math.abs(yDiff)));

		if (xDiff > 0)
		{
			xField.setText(String.valueOf(point.getX()));
		}
		if (yDiff > 0)
		{
			yField.setText(String.valueOf(point.getY()));
		}

		return true;
	}

	public void setWorldArea(WorldArea area)
	{
		xField.setText(String.valueOf(area.getX()));
		yField.setText(String.valueOf(area.getY()));
		planeField.setText(String.valueOf(area.getPlane()));
		widthField.setText(String.valueOf(area.getWidth()));
		heightField.setText(String.valueOf(area.getHeight()));
	}
}