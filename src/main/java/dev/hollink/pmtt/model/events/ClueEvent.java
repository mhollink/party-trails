package dev.hollink.pmtt.model.events;

public sealed interface ClueEvent permits AnimationEvent, InteractionEvent, MusicEvent, SkillEvent
{
}
