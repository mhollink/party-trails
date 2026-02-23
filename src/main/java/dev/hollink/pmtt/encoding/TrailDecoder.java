package dev.hollink.pmtt.encoding;

import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.AnagramStep;
import dev.hollink.pmtt.data.steps.CipherStep;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.CrypticStep;
import dev.hollink.pmtt.data.steps.EmoteStep;
import dev.hollink.pmtt.data.steps.SkillStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.TrailMetadata;
import dev.hollink.pmtt.data.trail.TrailProgress;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Optional;
import java.util.zip.InflaterInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrailDecoder
{
	private static final Map<StepType, StepDecoder> DECODERS = Map.ofEntries(
		entry(StepType.EMOTE_STEP, EmoteStep::decode),
		entry(StepType.CIPHER_STEP, CipherStep::decode),
		entry(StepType.COORDINATE_STEP, CoordsStep::decode),
		entry(StepType.CRYPTIC_STEP, CrypticStep::decode),
		entry(StepType.SKILL_STEP, SkillStep::decode),
		entry(StepType.ANAGRAM_STEP, AnagramStep::decode)
	);

	public static String readString(DataInput in) throws IOException
	{
		int length = in.readUnsignedShort();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static TreasureTrail decodeTrail(String encodedTrail) throws IOException
	{
		byte[] data = Base64.getUrlDecoder().decode(encodedTrail);
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		InflaterInputStream ii = new InflaterInputStream(bis);
		DataInput in = new DataInputStream(ii);

		validateMagicHeader(in);

		TrailMetadata metadata = decodeMetadata(in);
		List<TrailStep> steps = decodeTrailSteps(metadata.stepCount(), in);

		return new TreasureTrail(metadata.version(), metadata.trailId(), metadata.trailName(), metadata.author(), steps);
	}

	public static TrailProgress decodeProgress(String encoded) throws IOException
	{
		byte[] data = Base64.getUrlDecoder().decode(encoded);

		DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(data));

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

	private static void validateMagicHeader(DataInput in) throws IOException
	{
		int magic = in.readInt();
		if (magic != TrailEncoder.MAGIC)
		{
			throw new InvalidMagicHeaderException("Encoded trail string was invalid!");
		}
	}

	private static TrailMetadata decodeMetadata(DataInput in) throws IOException
	{
		int version = in.readInt();
		String trailId = readString(in);
		String trailName = readString(in);
		String author = readString(in);
		int stepCount = in.readShort();

		return new TrailMetadata(version, trailId, trailName, author, stepCount);
	}


	private static List<TrailStep> decodeTrailSteps(int stepCount, DataInput in) throws IOException
	{
		List<TrailStep> steps = new ArrayList<>();
		for (int i = 0; i < stepCount; i++)
		{
			decodeTrailStep(in).ifPresent(steps::add);
		}
		return steps;
	}

	private static Optional<TrailStep> decodeTrailStep(DataInput in) throws IOException
	{
		byte type = in.readByte();
		return getStepDecoder(type)
			.map(decoder -> tryDecode(type, in, decoder));
	}

	private static TrailStep tryDecode(byte type, DataInput in, StepDecoder decoder)
	{
		try {
			return decoder.decode(in);
		} catch (IOException e) {
			log.error("Unable to parse trail step ({})", StepType.fromByte(type), e);
			return null;
		}
	}

	private static Optional<StepDecoder> getStepDecoder(byte type)
	{
		return Optional.ofNullable(DECODERS.get(StepType.fromByte(type)));
	}

	@FunctionalInterface
	private interface StepDecoder
	{
		TrailStep decode(DataInput in) throws IOException;
	}

}
