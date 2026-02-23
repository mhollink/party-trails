package dev.hollink.pmtt.data.steps;

import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.StepType;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@ToString
@EqualsAndHashCode(callSuper = true)
public final class AnagramStep extends InteractionStep
{
	public AnagramStep(String cipherText, InteractionTarget target)
	{
		super(cipherText, target);
	}

	@Override
	public StepType type()
	{
		return StepType.ANAGRAM_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(cipherText, panel, graphics);
		drawTitle("Anagram clue", panel);
		drawText(cipherText, panel);
	}

	public static AnagramStep decode(DataInput in) throws IOException
	{
		String text = readString(in);
		int targetId = in.readInt();
		String targetName = readString(in);
		String action = readString(in);
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();
		return new AnagramStep(text, new InteractionTarget(targetId, targetName, action, new WorldPoint(x, y, plane)));
	}

}

