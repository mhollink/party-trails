package dev.hollink.partytrails.codec;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

public interface Decoder<T>
{

	default String readString(DataInput in) throws IOException
	{
		int length = in.readUnsignedShort();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	T decode(DataInput in) throws IOException;

	default T decode(String value) throws IOException
	{
		byte[] data = Base64.getUrlDecoder().decode(value);
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		InflaterInputStream ii = new InflaterInputStream(bis);
		DataInput in = new DataInputStream(ii);

		return decode(in);
	}
}
