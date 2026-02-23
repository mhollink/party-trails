package dev.hollink.pmtt.data.trail;

import dev.hollink.pmtt.data.StepType;
import java.io.DataOutput;
import java.io.IOException;

public interface Encodable
{
	StepType type();

	void encode(DataOutput out)
		throws IOException;
}
