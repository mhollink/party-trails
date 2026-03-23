package dev.hollink.partytrails.codec.steps;

import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.codec.Codec;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class StepCodecFactory
{
	@SuppressWarnings("unchecked")
	public static Codec<TrailStep> buildCodec(StepType type)
	{
		switch (type)
		{
			case EMOTE_STEP:
				return (Codec<TrailStep>) (Codec<?>) new EmoteStepCodec();
			case CIPHER_STEP:
				return (Codec<TrailStep>) (Codec<?>) new InteractionStepCodec(StepType.CIPHER_STEP);
			case COORDINATE_STEP:
				return (Codec<TrailStep>) (Codec<?>) new CoordinateStepCodec();
			case CRYPTIC_STEP:
				return (Codec<TrailStep>) (Codec<?>) new InteractionStepCodec(StepType.CRYPTIC_STEP);
			case SKILL_STEP:
				return (Codec<TrailStep>) (Codec<?>) new SkillStepCodec();
			case ANAGRAM_STEP:
				return (Codec<TrailStep>) (Codec<?>) new InteractionStepCodec(StepType.ANAGRAM_STEP);
			default:
				throw new IllegalArgumentException("Unknown step type: " + type);
		}
	}

	public static Map.Entry<StepType, Codec<TrailStep>> buildCodecEntry(StepType type)
	{
		return Map.entry(type, buildCodec(type));
	}

	public static Map<StepType, Codec<TrailStep>> createAllCodecs()
	{
		return Arrays.stream(StepType.values())
			.map(StepCodecFactory::buildCodecEntry)
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue
			));
	}
}
