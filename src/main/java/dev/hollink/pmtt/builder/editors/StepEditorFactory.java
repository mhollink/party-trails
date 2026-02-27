package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.data.StepType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public final class StepEditorFactory
{
	public static StepEditor create(StepType type)
	{
		switch (type)
		{
			case EMOTE_STEP:
				return new EmoteStepEditor();
			case COORDINATE_STEP:
				return new CoordinateStepEditor();
			case SKILL_STEP:
				return new SkillStepEditor();
			case CIPHER_STEP:
				return new ObjectInteractionStepEditor(StepType.CIPHER_STEP);
			case ANAGRAM_STEP:
				return new ObjectInteractionStepEditor(StepType.ANAGRAM_STEP);
			case CRYPTIC_STEP:
				return new ObjectInteractionStepEditor(StepType.CRYPTIC_STEP);
			default:
				throw new IllegalArgumentException("Unknown step type: " + type);
		}
	}
}
