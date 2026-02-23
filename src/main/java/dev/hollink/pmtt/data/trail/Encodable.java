package dev.hollink.pmtt.data.trail;

import java.io.DataOutput;
import java.io.IOException;

public interface Encodable
{
	byte typeId();

	void encode(DataOutput out)
		throws IOException;
}
