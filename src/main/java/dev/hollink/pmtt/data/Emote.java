package dev.hollink.pmtt.data;

import lombok.Getter;
import net.runelite.api.gameval.AnimationID;

@Getter
public enum Emote
{
	YES("Yes", AnimationID.EMOTE_YES),
	NO("No", AnimationID.EMOTE_NO),
	THINK("Think", AnimationID.EMOTE_THINK),
	BOW("Bow", AnimationID.EMOTE_BOW),
	ANGRY("Angry", AnimationID.EMOTE_ANGRY),
	CRY("Cry", AnimationID.EMOTE_CRY),
	LAUGH("Laugh", AnimationID.EMOTE_LAUGH),
	CHEER("Cheer", AnimationID.EMOTE_CHEER),
	WAVE("Wave", AnimationID.EMOTE_WAVE),
	BECKON("Beckon", AnimationID.EMOTE_BECKON),
	DANCE("Dance", AnimationID.EMOTE_DANCE),
	CLAP("Clap", AnimationID.EMOTE_CLAP),
	PANIC("Panic", AnimationID.EMOTE_PANIC),
	JIG("Jig", AnimationID.EMOTE_DANCE_SCOTTISH),
	SPIN("Spin", AnimationID.EMOTE_DANCE_SPIN),
	HEADBANG("Headbang", AnimationID.EMOTE_DANCE_HEADBANG),
	JUMP_FOR_JOY("Jump for Joy", AnimationID.EMOTE_JUMP_WITH_JOY),
	RASPBERRY("Raspberry", AnimationID.EMOTE_YA_BOO_SUCKS),
	YAWN("Yawn", AnimationID.EMOTE_YAWN),
	SALUTE("Salute", AnimationID.EMOTE_FREMMENIK_SALUTE),
	SHRUG("Shrug", AnimationID.EMOTE_SHRUG),
	BLOW_KISS("Blow Kiss", AnimationID.EMOTE_BLOW_KISS),
	SLAP_HEAD("Slap Head", AnimationID.EMOTE_SLAP_HEAD),
	STAMP("Stamp", AnimationID.EMOTE_STAMPFEET),
	FLAP("Flap", AnimationID.EMOTE_PANIC_FLAP),
	PUSH_UP("Push up", AnimationID.EMOTE_PUSHUPS_5),
	CRAB_DANCE("Crab Dance", AnimationID.HUMAN_EMOTE_CRABDANCE);

	private final String name;
	private final int animationId;

	Emote(String name, int emoteId)
	{
		this.name = name;
		this.animationId = emoteId;
	}
}