package dev.hollink.pmtt.builder.editors;

import dev.hollink.pmtt.data.StepType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepEditorFactory
{
	public static StepEditor create(StepType type)
	{
		return switch (type)
		{
			case EMOTE_STEP -> new EmoteStepEditor();
			case COORDINATE_STEP -> new CoordinateStepEditor();
			case SKILL_STEP -> new SkillStepEditor();
			case CIPHER_STEP -> new ObjectInteractionStepEditor(StepType.CIPHER_STEP);
			case ANAGRAM_STEP -> new ObjectInteractionStepEditor(StepType.ANAGRAM_STEP);
			case CRYPTIC_STEP -> new ObjectInteractionStepEditor(StepType.CRYPTIC_STEP);
		};
	}
}
