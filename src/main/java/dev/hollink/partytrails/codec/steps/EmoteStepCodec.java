package dev.hollink.partytrails.codec.steps;

import dev.hollink.partytrails.data.Emote;
import dev.hollink.partytrails.data.steps.EmoteStep;
import dev.hollink.partytrails.codec.Codec;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.runelite.api.coords.WorldPoint;


public class EmoteStepCodec implements Codec<EmoteStep>
{
	@Override
	public void encode(EmoteStep step, DataOutput out) throws IOException
	{
		writeString(out, step.getHint());
		out.writeInt(step.getTargetEmoteOne().ordinal());
		out.writeInt(step.getTargetLocation().getX());
		out.writeInt(step.getTargetLocation().getY());
		out.writeInt(step.getTargetLocation().getPlane());
	}

	@Override
	public EmoteStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		Emote emote = Emote.values()[in.readInt()];
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new EmoteStep(hint, emote, new WorldPoint(x, y, plane));
	}
}
