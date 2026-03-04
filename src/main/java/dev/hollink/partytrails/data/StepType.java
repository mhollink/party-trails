package dev.hollink.partytrails.data;

public enum StepType
{
	EMOTE_STEP((byte) 1, "Emote clue"),
	CIPHER_STEP((byte) 2, "Cipher clue"),
	COORDINATE_STEP((byte) 3, "Coordinate clue"),
	CRYPTIC_STEP((byte) 4, "Cryptic clue"),
	SKILL_STEP((byte) 5, "Skill clue"),
	ANAGRAM_STEP((byte) 6, "Anagram clue");

	public final byte stepTypeId;
	public final String stepTypeName;

	StepType(byte stepTypeId, String stepTypeName)
	{
		this.stepTypeId = stepTypeId;
		this.stepTypeName = stepTypeName;
	}

	public static StepType fromByte(byte stepTypeId)
	{
		switch (stepTypeId)
		{
			case 1:
				return EMOTE_STEP;
			case 2:
				return CIPHER_STEP;
			case 3:
				return COORDINATE_STEP;
			case 4:
				return CRYPTIC_STEP;
			case 5:
				return SKILL_STEP;
			case 6:
				return ANAGRAM_STEP;
			default:
				return null;
		}
	}
}
