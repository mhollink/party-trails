package dev.hollink.pmtt.model.steps;

import static dev.hollink.pmtt.crypto.TrailDecoder.readString;
import static dev.hollink.pmtt.crypto.TrailEncoder.writeString;
import dev.hollink.pmtt.model.StepTypes;
import dev.hollink.pmtt.model.events.AnimationEvent;
import dev.hollink.pmtt.model.events.ClueEvent;
import dev.hollink.pmtt.model.trail.ClueContext;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;
import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@RequiredArgsConstructor
public final class CoordsStep implements TrailStep
{

	private final String hint;
	private final WorldPoint targetLocation;

	@Override
	public byte typeId()
	{
		return StepTypes.COORDINATE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] coords = hint.split("\n");

		int textWidth = Math.max(ComponentConstants.STANDARD_WIDTH, Math.max(fontMetrics.stringWidth(coords[0]) + 10, fontMetrics.stringWidth(coords[1]) + 10));

		panel.setPreferredSize(new Dimension(textWidth, 0));

		panel.getChildren().add(TitleComponent.builder().text("Coordinate Clue").build());
		panel.getChildren().add(LineComponent.builder().left(coords[0]).build());
		panel.getChildren().add(LineComponent.builder().left(coords[1]).build());
	}

	@Override
	public void onActivate(ClueContext context)
	{
		// Noop.
	}

	@Override
	public boolean isComplete(ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent)
		{
			return animationEvent.animationId() == HUMAN_DIG && isInRange(targetLocation, animationEvent.location());
		}
		else
		{
			return false;
		}
	}

	@Override
	public void encode(DataOutput out) throws IOException
	{
		writeString(out, hint);
		out.writeInt(targetLocation.getX());
		out.writeInt(targetLocation.getY());
		out.writeInt(targetLocation.getPlane());
	}

	public static CoordsStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new CoordsStep(hint, new WorldPoint(x, y, plane));
	}
}
