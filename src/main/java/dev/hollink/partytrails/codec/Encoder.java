package dev.hollink.partytrails.codec;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;

public interface Encoder<T>
{
	default void writeString(DataOutput out, String value) throws IOException
	{
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		out.writeShort(bytes.length);
		out.write(bytes);
	}

	void encode(T value, DataOutput out) throws IOException;

	default String encodeToString(T value) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(bos);
		DataOutputStream out = new DataOutputStream(dos);

		encode(value, out);

		out.flush();
		dos.finish();

		return Base64.getUrlEncoder()
			.withoutPadding()
			.encodeToString(bos.toByteArray());
	}
}
