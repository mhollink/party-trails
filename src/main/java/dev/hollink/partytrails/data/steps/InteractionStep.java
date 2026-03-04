package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.InteractionTarget;
import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.events.InteractionEvent;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.encoding.TrailDecoder;
import static dev.hollink.partytrails.encoding.TrailDecoder.readString;
import static dev.hollink.partytrails.encoding.TrailEncoder.writeString;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@ToString
@EqualsAndHashCode
public final class InteractionStep implements TrailStep
{
	public static final List<StepType> ALLOWED_TYPES = List.of(StepType.ANAGRAM_STEP, StepType.CIPHER_STEP, StepType.CRYPTIC_STEP);

	private final StepType stepType;
	private final String hint;
	private final InteractionTarget target;

	public InteractionStep(StepType stepType, String hint, InteractionTarget target)
	{
		if (stepType == null || !ALLOWED_TYPES.contains(stepType)) {
			throw new IllegalArgumentException("Invalid step type: " + stepType);
		}

		this.stepType = stepType;
		this.hint = hint;
		this.target = target;
	}

	public StepType type()
	{
		return stepType;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle(stepType.stepTypeName, panel);
		drawText(hint, panel);
	}


	@Override
	public void onActivate(TrailContext context)
	{
		// noop
	}

	@Override
	public boolean handlesEvent(TrailEvent event)
	{
		return event instanceof InteractionEvent;
	}

	@Override
	public boolean isComplete(TrailContext context, TrailEvent event)
	{
		if (event instanceof InteractionEvent)
		{
			InteractionEvent interactionEvent = (InteractionEvent) event;
			return target.checkEvent(interactionEvent);
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
		out.writeInt(target.getTargetId());
		writeString(out, target.getTargetName());
		writeString(out, target.getInteractionType());
		out.writeInt(target.getLocation().getX());
		out.writeInt(target.getLocation().getY());
		out.writeInt(target.getLocation().getPlane());
	}

	public static TrailDecoder.StepDecoder getDecoder(StepType stepType)
	{
		return (DataInput in) -> {
			String text = readString(in);
			int targetId = in.readInt();
			String targetName = readString(in);
			String action = readString(in);
			int x = in.readInt();
			int y = in.readInt();
			int plane = in.readInt();

			return new InteractionStep(stepType, text, new InteractionTarget(targetId, targetName, action, new WorldPoint(x, y, plane)));
		};
	}


}
