package dev.hollink.partytrails.codec;

import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailMetadata;
import dev.hollink.partytrails.codec.exceptions.InvalidMagicHeaderException;
import dev.hollink.partytrails.codec.steps.StepCodecFactory;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class TrailCodec implements Codec<TreasureTrail>
{
	public static final int MAGIC = 0x54524C31;

	private final Map<StepType, Codec<TrailStep>> stepCodecs;

	@Inject
	public TrailCodec()
	{
		stepCodecs = StepCodecFactory.createAllCodecs();
	}

	@Override
	public void encode(TreasureTrail treasureTrail, DataOutput out) throws IOException
	{
		// Encoding a magic header for input validation
		out.writeInt(MAGIC);

		encodeMetadata(treasureTrail, out);
		encodeSteps(treasureTrail, out);
	}

	private void encodeMetadata(TreasureTrail trail, DataOutput out) throws IOException
	{
		out.writeInt(trail.getMetadata().getVersion());
		writeString(out, trail.getMetadata().getTrailId());
		writeString(out, trail.getMetadata().getTrailName());
		writeString(out, trail.getMetadata().getAuthor());
	}

	private void encodeSteps(TreasureTrail trail, DataOutput out) throws IOException
	{
		List<TrailStep> steps = trail.getSteps();
		out.writeShort(steps.size());
		for (TrailStep step : steps)
		{
			out.writeByte(step.getStepType().stepTypeId);
			stepCodecs.get(step.getStepType()).encode(step, out);
		}
	}

	@Override
	public TreasureTrail decode(DataInput in) throws IOException
	{
		validateMagicHeader(in);

		TrailMetadata metadata = decodeMetadata(in);
		List<TrailStep> steps = decodeTrailSteps(metadata.getStepCount(), in);

		return new TreasureTrail(
			metadata.getVersion(),
			metadata.getTrailId(),
			metadata.getTrailName(),
			metadata.getAuthor(),
			steps);
	}

	private TrailMetadata decodeMetadata(DataInput in) throws IOException
	{
		int version = in.readInt();
		String trailId = readString(in);
		String trailName = readString(in);
		String author = readString(in);
		int stepCount = in.readShort();

		return new TrailMetadata(version, trailId, trailName, author, stepCount);
	}

	private List<TrailStep> decodeTrailSteps(int stepCount, DataInput in) throws IOException
	{
		List<TrailStep> steps = new ArrayList<>();
		for (int i = 0; i < stepCount; i++)
		{
			byte type = in.readByte();
			StepType stepType = StepType.fromByte(type);
			Codec<TrailStep> decoder = stepCodecs.get(stepType);
			TrailStep step = decoder.decode(in);
			steps.add(step);
		}
		return steps;
	}

	private static void validateMagicHeader(DataInput in) throws IOException
	{
		int magic = in.readInt();
		if (magic != MAGIC)
		{
			throw new InvalidMagicHeaderException("Encoded trail string was invalid!");
		}
	}
}
