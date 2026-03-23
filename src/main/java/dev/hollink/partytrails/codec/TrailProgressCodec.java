package dev.hollink.partytrails.codec;

import dev.hollink.partytrails.data.trail.TrailProgress;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrailProgressCodec implements Codec<TrailProgress>
{

	@Override
	public TrailProgress decode(DataInput in) throws IOException
	{
		String trailId = readString(in);
		int index = in.readUnsignedShort();
		boolean completed = in.readBoolean();
		int stateSize = in.readUnsignedShort();

		Map<String, String> state = new HashMap<>();
		for (int i = 0; i < stateSize; i++)
		{
			String key = readString(in);
			String value = readString(in);
			state.put(key, value);
		}

		return new TrailProgress(trailId, index, completed, state);
	}

	@Override
	public void encode(TrailProgress progress, DataOutput out) throws IOException
	{
		writeString(out, progress.getTrailId());
		out.writeInt(progress.getCurrentStepIndex());
		out.writeBoolean(progress.isCompleted());

		Map<String, String> state = progress.getStepState();
		out.writeShort(state.size());
		for (var entry : state.entrySet())
		{
			writeString(out, entry.getKey());
			writeString(out, entry.getValue());
		}
	}
}
