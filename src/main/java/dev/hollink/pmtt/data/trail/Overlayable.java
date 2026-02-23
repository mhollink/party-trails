package dev.hollink.pmtt.data.trail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Arrays;
import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public interface Overlayable
{

	void drawOverlay(PanelComponent panel, Graphics2D graphics);

	default int textWidth(String text, Graphics2D graphics)
	{
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] parts = text.split("\n");
		int maxLineWidth = Arrays.stream(parts)
			.mapToInt(fontMetrics::stringWidth)
			.max().orElse(0);
		return Math.max(ComponentConstants.STANDARD_WIDTH, maxLineWidth + 10);
	}

	default void setPanelWidth(String text, PanelComponent panel, Graphics2D graphics) {
		int textWidth = textWidth(text, graphics);
		panel.setPreferredSize(new Dimension(textWidth, 0));
	}

	default void drawTitle(String text, PanelComponent panel)
	{
		panel.getChildren().add(TitleComponent.builder().text(text).build());
	}

	default void drawText(String text, PanelComponent panel, Color color)
	{
		String[] parts = text.split("\n");
		for (String part : parts)
		{
			panel.getChildren().add(LineComponent.builder().left(part).leftColor(color).build());
		}
	}

	default void drawText(String text, PanelComponent panel)
	{
		drawText(text, panel, TITLED_CONTENT_COLOR);
	}
}
