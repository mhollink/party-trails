package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.Emote;
import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class EmoteStep implements TrailStep
{

	private final String hint;
	private final Emote targetEmoteOne;
	private final WorldPoint targetLocation;

	@Override
	public StepType type()
	{
		return StepType.EMOTE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Emote Clue", panel);
		drawText("Emote:", panel, Color.WHITE);
		drawText(targetEmoteOne.getName(), panel);
		drawText("Location:", panel, Color.WHITE);
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
		if (event instanceof AnimationEvent)
		{
			AnimationEvent animationEvent = (AnimationEvent) event;
			return animationEvent.getAnimationId() == targetEmoteOne.getAnimationId()
				&& animationEvent.getLocation().distanceTo(targetLocation) <= DEFAULT_LOCATION_DISTANCE;
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
		out.writeInt(targetEmoteOne.ordinal());
		out.writeInt(targetLocation.getX());
		out.writeInt(targetLocation.getY());
		out.writeInt(targetLocation.getPlane());
	}

	public static EmoteStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		Emote emote = Emote.values()[in.readInt()];
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new EmoteStep(hint, emote, new WorldPoint(x, y, plane));
	}
}


