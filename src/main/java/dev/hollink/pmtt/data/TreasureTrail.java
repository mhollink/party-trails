package dev.hollink.pmtt.data;

import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.TrailMetadata;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreasureTrail
{
	private final TrailMetadata metadata;
	private final List<TrailStep> steps;

	public TreasureTrail(int version, String id, String name, String author, List<TrailStep> steps)
	{
		this.metadata = new TrailMetadata(version, id, name, author, steps.size());
		this.steps = steps;
	}

	public Optional<TrailStep> getStep(int index)
	{
		try
		{
			return Optional.ofNullable(steps.get(index));
		}
		catch (IndexOutOfBoundsException e)
		{
			return Optional.empty();
		}
	}
}
