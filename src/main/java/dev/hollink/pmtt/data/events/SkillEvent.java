package dev.hollink.pmtt.data.events;

import lombok.Value;
import net.runelite.api.Skill;

@Value
public class SkillEvent implements ClueEvent
{
	Skill skill;
	int xp;
}
