package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import static net.runelite.api.gameval.AnimationID.HUMAN_DIG;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class CoordsStep implements TrailStep
{

	private final String hint;
	private final WorldPoint targetLocation;

	@Override
	public StepType type()
	{
		return StepType.COORDINATE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Coordinate clue", panel);
		drawText(hint, panel);
	}

	@Override
	public void onActivate(ClueContext context)
	{
		// Noop.
	}

	@Override
	public boolean handlesEvent(ClueEvent event)
	{
		return event instanceof AnimationEvent;
	}

	@Override
	public boolean isComplete(ClueContext context, ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent)
		{
			log.info("Validating clue step...");
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
