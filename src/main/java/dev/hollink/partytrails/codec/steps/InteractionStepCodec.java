package dev.hollink.partytrails.codec.steps;

import dev.hollink.partytrails.codec.Codec;
import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.steps.InteractionStep;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
public class InteractionStepCodec implements Codec<InteractionStep>
{
	private final StepType stepType;

	@Override
	public void encode(InteractionStep step, DataOutput out) throws IOException
	{
		writeString(out, step.getHint());
		out.writeInt(step.getTarget().getTargetId());
		writeString(out, step.getTarget().getTargetName());
		writeString(out, step.getTarget().getInteractionType());
		out.writeInt(step.getTarget().getLocation().getX());
		out.writeInt(step.getTarget().getLocation().getY());
		out.writeInt(step.getTarget().getLocation().getPlane());
	}

	@Override
	public InteractionStep decode(DataInput in) throws IOException
	{
		String text = readString(in);
		int targetId = in.readInt();
		String targetName = readString(in);
		String action = readString(in);
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new InteractionStep(stepType, text, new InteractionStep.Target(targetId, targetName, action, new WorldPoint(x, y, plane)));
	}
}
