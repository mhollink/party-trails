package dev.hollink.pmtt.data.steps;

import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import dev.hollink.pmtt.data.InteractionTarget;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.events.InteractionEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
	public boolean isComplete(ClueEvent event)
	{
		if (event instanceof InteractionEvent interactionEvent)
		{
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
		out.writeInt(target.targetId());
		writeString(out, target.targetName());
		writeString(out, target.interactionType());
		out.writeInt(target.location().getX());
		out.writeInt(target.location().getY());
		out.writeInt(target.location().getPlane());
	}
}
