package dev.hollink.pmtt.crypto;

import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.model.steps.TrailStep;
import dev.hollink.pmtt.model.trail.Encodable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class TrailEncoder
{
	private static final int MAGIC = 0x54524C31;

	public static String encode(TreasureTrail trail) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bos);

		out.writeInt(MAGIC);
		out.writeByte(trail.getVersion()); // version

		writeString(out, trail.getTrailId());
		writeString(out, trail.getTrailName());
		writeString(out, trail.getAuthor());

		List<TrailStep> steps = trail.getSteps();
		out.writeShort(steps.size());

		for (Encodable step : steps)
		{
			out.writeByte(step.typeId());
			step.encode(out);
		}

		out.flush();

		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(bos.toByteArray());
	}

	public static void writeString(DataOutput out, String value) throws IOException
	{
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		out.writeShort(bytes.length);
		out.write(bytes);
	}
}
