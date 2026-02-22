package dev.hollink.pmtt.model;

import dev.hollink.pmtt.model.steps.TrailStep;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreasureTrail
{

	private final int version;
	private final String trailId;
	private final String trailName;
	private final String author;
	private final List<TrailStep> steps;

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

	public int getStepCount()
	{
		return steps.size();
	}
}
