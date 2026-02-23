package dev.hollink.pmtt.data;

public enum StepType
{
	EMOTE_STEP((byte) 1),
	CIPHER_STEP((byte) 2),
	COORDINATE_STEP((byte) 3),
	CRYPTIC_STEP((byte) 4),
	SKILL_STEP((byte) 5),
	ANAGRAM_STEP((byte) 6);

	public final byte stepTypeId;

	StepType(byte stepTypeId)
	{
		this.stepTypeId = stepTypeId;
	}

	public static StepType fromByte(byte stepTypeId) {
		return switch (stepTypeId)
		{
			case 1 -> EMOTE_STEP;
			case 2 -> CIPHER_STEP;
			case 3 -> COORDINATE_STEP;
			case 4 -> CRYPTIC_STEP;
			case 5 -> SKILL_STEP;
			case 6 -> ANAGRAM_STEP;
			default -> null;
		};
	}
}
