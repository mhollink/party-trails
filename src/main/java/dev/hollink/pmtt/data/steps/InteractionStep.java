package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.events.InteractionEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class InteractionStep implements TrailStep
{

	protected final String cipherText;
	protected final InteractionTarget target;

	@Override
	public void onActivate(ClueContext context)
	{
		// noop
	}

	@Override
	public boolean handlesEvent(ClueEvent event)
	{
		return event instanceof InteractionEvent;
	}

	@Override
	public boolean isComplete(ClueContext context, ClueEvent event)
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
		writeString(out, cipherText);
		out.writeInt(target.getTargetId());
		writeString(out, target.getTargetName());
		writeString(out, target.getInteractionType());
		out.writeInt(target.getLocation().getX());
		out.writeInt(target.getLocation().getY());
		out.writeInt(target.getLocation().getPlane());
	}
}
