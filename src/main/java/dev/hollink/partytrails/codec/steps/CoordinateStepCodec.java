package dev.hollink.partytrails.codec.steps;

import dev.hollink.partytrails.data.steps.CoordsStep;
import dev.hollink.partytrails.codec.Codec;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.runelite.api.coords.WorldPoint;


public class CoordinateStepCodec implements Codec<CoordsStep>
{
	@Override
	public void encode(CoordsStep step, DataOutput out) throws IOException
	{
		writeString(out, step.getHint());
		out.writeInt(step.getTargetLocation().getX());
		out.writeInt(step.getTargetLocation().getY());
		out.writeInt(step.getTargetLocation().getPlane());
	}

	@Override
	public CoordsStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new CoordsStep(hint, new WorldPoint(x, y, plane));
	}
}
