package dev.hollink.pmtt.data.steps;

import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.StepType;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Slf4j
@ToString
@EqualsAndHashCode(callSuper = true)
public final class CrypticStep extends InteractionStep
{
	public CrypticStep(String cipherText, InteractionTarget target)
	{
		super(cipherText, target);
	}

	@Override
	public StepType type()
	{
		return StepType.CRYPTIC_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] parts = cipherText.split("\n");
		int maxLineWidth = Arrays.stream(parts)
			.mapToInt(fontMetrics::stringWidth)
			.max().orElse(0);
		int textWidth = Math.max(ComponentConstants.STANDARD_WIDTH, maxLineWidth + 10);
		panel.setPreferredSize(new Dimension(textWidth, 0));

		panel.getChildren().add(TitleComponent.builder().text("Cryptic Clue").build());
		for (String part : parts)
		{
			panel.getChildren().add(LineComponent.builder().left(part).build());
		}
	}

	public static CrypticStep decode(DataInput in)
		throws IOException
	{
		String text = readString(in);
		int targetId = in.readInt();
		String targetName = readString(in);
		String action = readString(in);
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();
		return new CrypticStep(text, new InteractionTarget(targetId, targetName, action, new WorldPoint(x, y, plane)));
	}

}
