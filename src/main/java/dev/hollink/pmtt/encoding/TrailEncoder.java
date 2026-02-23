package dev.hollink.pmtt.encoding;

import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.Encodable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

public class TrailEncoder
{
	public static final int MAGIC = 0x54524C31;

	public static void writeString(DataOutput out, String value) throws IOException
	{
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		out.writeShort(bytes.length);
		out.write(bytes);
	}

	public static String encode(TreasureTrail trail) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(bos);
		DataOutputStream out = new DataOutputStream(dos);

		// Encoding a magic header for input validation, see Decoder#validateMagicHeader
		out.writeInt(MAGIC);

		encodeMetadata(trail, out);
		encodeSteps(trail, out);

		out.flush();
		dos.finish();

		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(bos.toByteArray());
	}

	private static void encodeSteps(TreasureTrail trail, DataOutputStream out) throws IOException
	{
		List<TrailStep> steps = trail.getSteps();
		out.writeShort(steps.size());
		for (Encodable step : steps)
		{
			encodeStep(out, step);
		}
	}

	private static void encodeStep(DataOutputStream out, Encodable step) throws IOException
	{
		out.writeByte(step.type().stepTypeId);
		step.encode(out);
	}

	private static void encodeMetadata(TreasureTrail trail, DataOutputStream out) throws IOException
	{
		out.writeInt(trail.getMetadata().version());
		writeString(out, trail.getMetadata().trailId());
		writeString(out, trail.getMetadata().trailName());
		writeString(out, trail.getMetadata().author());
	}
}
