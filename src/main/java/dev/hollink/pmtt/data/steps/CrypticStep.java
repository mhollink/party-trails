package dev.hollink.pmtt.data.steps;

import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.StepTypes;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.IOException;
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
	public byte typeId()
	{
		return StepTypes.CRYPTIC_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		int textWidth = Math.max(ComponentConstants.STANDARD_WIDTH, fontMetrics.stringWidth(cipherText) + 10);

		panel.setPreferredSize(new Dimension(textWidth, 0));

		panel.getChildren().add(TitleComponent.builder().text("Cryptic Clue").build());
		panel.getChildren().add(LineComponent.builder().left(cipherText).build());
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
