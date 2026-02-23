package dev.hollink.pmtt.data.events;

import net.runelite.api.Skill;

public record SkillEvent(Skill skill, int xp) implements ClueEvent
{
}
