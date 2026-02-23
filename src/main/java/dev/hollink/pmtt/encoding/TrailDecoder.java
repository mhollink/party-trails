package dev.hollink.pmtt.encoding;

import dev.hollink.pmtt.data.StepTypes;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.AnagramStep;
import dev.hollink.pmtt.data.steps.CipherStep;
import dev.hollink.pmtt.data.steps.CoordsStep;
import dev.hollink.pmtt.data.steps.CrypticStep;
import dev.hollink.pmtt.data.steps.EmoteStep;
import dev.hollink.pmtt.data.steps.SkillStep;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.TrailMetadata;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import java.util.zip.InflaterInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrailDecoder
{
	private static final Map<Byte, StepDecoder> DECODERS = Map.ofEntries(
		entry(StepTypes.EMOTE_STEP, EmoteStep::decode),
		entry(StepTypes.CIPHER_STEP, CipherStep::decode),
		entry(StepTypes.COORDINATE_STEP, CoordsStep::decode),
		entry(StepTypes.CRYPTIC_STEP, CrypticStep::decode),
		entry(StepTypes.SKILL_STEP, SkillStep::decode),
		entry(StepTypes.ANAGRAM_STEP, AnagramStep::decode)
	);

	public static String readString(DataInput in) throws IOException
	{
		int length = in.readUnsignedShort();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static TreasureTrail decode(String encodedTrail) throws IOException
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

	private static void validateMagicHeader(DataInput in) throws IOException
	{
		int magic = in.readInt();
		if (magic != TrailEncoder.MAGIC)
		{
			throw new IllegalArgumentException("Invalid trail format");
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
			TrailStep decodedStep = decodeTrailStep(in);
			steps.add(decodedStep);
		}
		return steps;
	}

	private static TrailStep decodeTrailStep(DataInput in) throws IOException
	{
		byte type = in.readByte();
		StepDecoder decoder = getStepDecoder(type);
		return decoder.decode(in);
	}

	private static StepDecoder getStepDecoder(byte type)
	{
		StepDecoder decoder = DECODERS.get(type);
		if (decoder == null)
		{
			throw new IllegalArgumentException("Unknown step type: " + type);
		}
		return decoder;
	}

	@FunctionalInterface
	private interface StepDecoder
	{
		TrailStep decode(DataInput in) throws IOException;
	}

}
